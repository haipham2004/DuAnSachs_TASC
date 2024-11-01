package com.example.users_service.service;

import com.example.users_service.security.request.LoginRequest;
import com.example.users_service.security.request.SignupRequest;
import com.example.users_service.security.response.LoginResponse;
import com.example.users_service.security.response.MessageResponse;
import com.example.users_service.security.response.SignupResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    LoginResponse authenticateUser(LoginRequest loginRequest);

    MessageResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest);

}
