package com.example.users_service.service;

import com.example.users_service.dto.request.LoginRequest;
import com.example.users_service.dto.request.SignupRequest;
import com.example.users_service.dto.request.TokenRequest;
import com.example.users_service.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    LoginResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response);

    void registerUser(SignupRequest signUpRequest);

    LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) ;

    LoginResponse fetchAccount(HttpServletRequest request);

    void logout(HttpServletRequest request);

    LoginResponse checkToken(TokenRequest tokenRequest);
}
