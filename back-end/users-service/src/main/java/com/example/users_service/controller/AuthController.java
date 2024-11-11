package com.example.users_service.controller;

import com.example.users_service.dto.request.TokenRequest;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    private JwtUtils jwtUtils;


    @Autowired
    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
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

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        boolean isValid = jwtUtils.validateJwtToken(token);
        if (isValid) {
            return ResponseEntity.ok("Token is valid");
        } else {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }
    }

    @PostMapping("public/checkToKen")
    public ApiResponse<LoginResponse> checkToken(@RequestBody TokenRequest tokenRequest) {
        return ApiResponse.<LoginResponse>builder().statusCode(200).message("Check token finish").results(authService.checkToken(tokenRequest)).build();
    }

}
