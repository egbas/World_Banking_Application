package com.egbas.World.Banking.service;

import com.egbas.World.Banking.payload.request.LoginRequest;
import com.egbas.World.Banking.payload.request.UserRequest;
import com.egbas.World.Banking.payload.response.APIResponse;
import com.egbas.World.Banking.payload.response.BankResponse;
import com.egbas.World.Banking.payload.response.JwtAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    BankResponse registerUser(UserRequest userRequest);

    ResponseEntity<APIResponse<JwtAuthResponse>> login(LoginRequest loginRequest);
}
