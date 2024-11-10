package com.example.users_service.controller;

import com.example.users_service.dto.response.ApiResponse;
import com.example.users_service.security.jwt.JwtUtils;
import com.example.users_service.dto.request.LoginRequest;
import com.example.users_service.dto.request.SignupRequest;
import com.example.users_service.dto.response.LoginResponse;
import com.example.users_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;



    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.authenticateUser(loginRequest, response);
        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/public/signup")
    public ApiResponse<Void> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ApiResponse.<Void>builder()
                .statusCode(200).message("Đăng ký success").build();
    }

    @GetMapping("/public/refreshToken")
    public ApiResponse<LoginResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return ApiResponse.<LoginResponse>builder().results(authService.refreshToken(request,response)).build();
    }

    @GetMapping("/public/fetchAccount")
    public ApiResponse<LoginResponse> getUserInfo(HttpServletRequest httpServletRequest){
       return ApiResponse.<LoginResponse>builder().results(authService.fetchAccount(httpServletRequest)).build();
    }

    @PostMapping("/public/logout")
    public ApiResponse<Void>  logoutUser(HttpServletRequest request) {
        authService.logout(request);
        return ApiResponse.<Void>builder()
                .statusCode(200).message("Logout success").build();
    }

}
