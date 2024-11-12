package com.example.api_gateway_service.service;


import com.example.api_gateway_service.dto.request.LoginRequest;
import com.example.api_gateway_service.dto.response.ApiResponse;
import com.example.api_gateway_service.dto.response.LoginResponse;
import com.example.api_gateway_service.repository.UserRepositoryClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsersService {
    private final UserRepositoryClient userRepositoryClient;

    public UsersService(@Lazy UserRepositoryClient userRepositoryClient) {
        this.userRepositoryClient = userRepositoryClient;
    }

    public Mono<ApiResponse<LoginResponse>> validateToken(String token) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setToken(token);
        return userRepositoryClient.checkToken(loginRequest);
    }
}
