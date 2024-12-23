package com.example.api_gateway_service.service;


import com.example.api_gateway_service.dto.request.LoginRequest;
import com.example.api_gateway_service.dto.response.ApiResponse;
import com.example.api_gateway_service.dto.response.LoginResponse;
import com.example.api_gateway_service.repository.UserRepositoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UsersService {

    private final UserRepositoryClient userRepositoryClient;

    @Autowired
    public UsersService(UserRepositoryClient userRepositoryClient) {
        this.userRepositoryClient = userRepositoryClient;
    }

    public Mono<ApiResponse<LoginResponse>> checkToken(String token) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setToken(token);  // Set token trong request
        return userRepositoryClient.checkToken(loginRequest);  // Gọi Feign Client để kiểm tra token
    }
}
