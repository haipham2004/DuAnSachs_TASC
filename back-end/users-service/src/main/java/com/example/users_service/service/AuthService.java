package com.example.users_service.service;

import com.example.users_service.dto.request.LoginRequest;
import com.example.users_service.dto.request.SignupRequest;
import com.example.users_service.dto.response.LoginResponse;
import com.example.users_service.dto.response.LogoutResponse;
import com.example.users_service.dto.response.MessageResponse;
import com.example.users_service.dto.response.TokensResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {

    LoginResponse authenticateUser(LoginRequest loginRequest);

    MessageResponse registerUser(SignupRequest signUpRequest);

    TokensResponse refreshToken(HttpServletRequest request);

    LogoutResponse logout(HttpServletRequest request);
}
