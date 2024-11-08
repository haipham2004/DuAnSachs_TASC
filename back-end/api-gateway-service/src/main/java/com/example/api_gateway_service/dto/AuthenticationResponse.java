package com.example.api_gateway_service.dto;

public class AuthenticationResponse {

    private int statusCode;

    private String error;

    private String message;

    private String token;

    private String refreshToken;

    private String expirationTime;

    private boolean isVaild;

    private  String role;
}
