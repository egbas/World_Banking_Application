package com.egbas.World.Banking.infrastructure.controller;

import com.egbas.World.Banking.payload.request.LoginRequest;
import com.egbas.World.Banking.payload.request.UserRequest;
import com.egbas.World.Banking.payload.response.APIResponse;
import com.egbas.World.Banking.payload.response.BankResponse;
import com.egbas.World.Banking.payload.response.JwtAuthResponse;
import com.egbas.World.Banking.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor
@Tag(name = "User Authentication Management APIs")
public class AuthController {
    private final AuthService authService;
    @Operation(
            summary = "Register New User Account",
            description = "Create a new user and assign account number"
    )
    @PostMapping("register-user")
    public BankResponse createAccount(@Valid @RequestBody UserRequest userRequest){
        return authService.registerUser(userRequest);
    }

    @PostMapping("login-user")
    public ResponseEntity<APIResponse<JwtAuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

}
