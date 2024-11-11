package com.example.api_gateway_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {

    private String jwtToken;
    private String jwtTokenRefresh;
    private String username;
    private List<String> roles;
    private String message;
    private String email;
    private String phone;

}