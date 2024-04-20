package com.egbas.World.Banking.payload.request;

import com.egbas.World.Banking.validations.ValidEmail;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    @Size(min = 2, max = 35, message = "First name must be at least 2 characters")
    @NotBlank(message = "First name must not be empty")
    private String firstName;

    private String middleName;

    @Size(min = 2, max = 35, message = "Last name must be at least 2 characters")
    @NotBlank(message = "Last name must not be empty")
    private String lastName;

    @Size(min = 11, max = 15, message = "Phone number is too short or too long")
    @NotBlank(message = "Phone number must not be empty")
    @Digits(fraction = 0, integer = 11, message = "Phone number is incorrect")
    private String phoneNumber;

    private String alternativePhoneNumber;

    @ValidEmail
    @Email(message = "Invalid email")
    @NotBlank(message = "email must not be empty")
    private String email;

    @NotBlank(message = "Password must not be empty")
    private String password;

    private String stateOfOrigin;

    private String address;

    private String gender;

}
