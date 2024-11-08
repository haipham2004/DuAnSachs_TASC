package com.example.users_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
//    @JsonProperty("access_token")
    private String jwtToken;
    private String jwtTokenRefresh;
    private String username;
    private List<String> roles;
    private String message;
    private String email;
    private String phone;

    public LoginResponse(String username, List<String> roles, String email, String phone) {
        this.username = username;
        this.roles = roles;
        this.email = email;
        this.phone = phone;
    }

    public LoginResponse(String jwtToken, String username, List<String> roles, String message, String email, String phone) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
        this.message = message;
        this.email = email;
        this.phone = phone;
    }

    public LoginResponse(String jwtToken, String jwtTokenRefresh, String username, List<String> roles, String message, String email, String phone) {
        this.jwtToken = jwtToken;
        this.jwtTokenRefresh=jwtTokenRefresh;
        this.username = username;
        this.roles = roles;
        this.message = message;
        this.email = email;
        this.phone = phone;
    }
}