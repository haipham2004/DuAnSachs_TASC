package com.example.api_gateway_service.repository;

import com.example.api_gateway_service.dto.request.LoginRequest;
import com.example.api_gateway_service.dto.response.ApiResponse;
import com.example.api_gateway_service.dto.response.LoginResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepositoryClient {

    @PostExchange(url = "http://localhost:8801/api/auth/public/checkToKen", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<LoginResponse>> checkToken(@RequestBody LoginRequest loginRequest);
}