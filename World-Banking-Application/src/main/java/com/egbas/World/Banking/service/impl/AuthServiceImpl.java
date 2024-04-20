package com.egbas.World.Banking.service.impl;

import com.egbas.World.Banking.domain.entities.UserEntity;
import com.egbas.World.Banking.domain.enums.Roles;
import com.egbas.World.Banking.infrastructure.config.JwtTokenProvider;
import com.egbas.World.Banking.payload.request.EmailDetails;
import com.egbas.World.Banking.payload.request.LoginRequest;
import com.egbas.World.Banking.payload.request.UserRequest;
import com.egbas.World.Banking.payload.response.APIResponse;
import com.egbas.World.Banking.payload.response.AccountInfo;
import com.egbas.World.Banking.payload.response.BankResponse;
import com.egbas.World.Banking.payload.response.JwtAuthResponse;
import com.egbas.World.Banking.repository.UserRepository;
import com.egbas.World.Banking.service.AuthService;
import com.egbas.World.Banking.service.EmailService;
import com.egbas.World.Banking.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;
    @Override
    public BankResponse registerUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .middleName(userRequest.getMiddleName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .status(userRequest.getAddress())
                .address(userRequest.getAddress())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(encoder.encode(userRequest.getPassword()))
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .roles(Roles.USER)
                .profilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png")
                .build();

        UserEntity saveUser = userRepository.save(newUser);

        //send email alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("CONGRATULATIONS!!! Your Account Has Been Successfully Created. \n Your account details: \n" +
                        "Account Name: " + saveUser.getFirstName() + " " + saveUser.getMiddleName() + " " + saveUser.getLastName() +
                        "\nAccount Number: " + saveUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(saveUser.getAccountNumber())
                        .accountBalance(saveUser.getAccountBalance())
                        .accountName(saveUser.getFirstName() + " " +
                                saveUser.getLastName() + " " +
                                saveUser.getMiddleName())
                        .build())
                .build();
    }

    @Override
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest) {

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(loginRequest.getEmail());

        Authentication authentication = null;

        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You are logged in")
                .recipient(loginRequest.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request, contact support desk")
                .build();

        emailService.sendEmailAlert(loginAlert);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        UserEntity userEntity = userEntityOptional.get();

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new APIResponse<>(
                                "Login Successful",
                                JwtAuthResponse.builder()
                                        .accessToken(token)
                                        .tokenType("Bearer")
                                        .id(userEntity.getId())
                                        .email(userEntity.getEmail())
                                        .gender(userEntity.getGender())
                                        .firstName(userEntity.getFirstName())
                                        .lastName(userEntity.getLastName())
                                        .profilePicture(userEntity.getProfilePicture())
                                        .role(userEntity.getRoles())
                                        .build()
                        )
                );
    }
}
