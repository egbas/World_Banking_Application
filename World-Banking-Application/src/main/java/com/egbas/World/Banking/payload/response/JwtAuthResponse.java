package com.egbas.World.Banking.payload.response;

import com.egbas.World.Banking.domain.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtAuthResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String profilePicture;

    private String email;

    private String gender;

    private Roles role;

    private String accessToken;

    private String tokenType = "Bearer";
}
