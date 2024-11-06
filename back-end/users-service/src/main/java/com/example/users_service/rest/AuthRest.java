package com.example.users_service.rest;

import com.example.users_service.security.jwt.JwtUtils;
import com.example.users_service.dto.request.LoginRequest;
import com.example.users_service.dto.request.SignupRequest;
import com.example.users_service.dto.response.LoginResponse;
import com.example.users_service.dto.response.LogoutResponse;
import com.example.users_service.dto.response.TokensResponse;
import com.example.users_service.security.service.UserDetailsImpl;
import com.example.users_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthRest {

    private AuthService authService;

    private UserDetailsService userDetailsService;

    private JwtUtils jwtUtils;

    @Autowired
    public AuthRest(AuthService authService, UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return ResponseEntity.ok(authService.registerUser(signUpRequest));
    }

    @GetMapping("/public/refreshToken")
    public ResponseEntity<TokensResponse> refreshToken(HttpServletRequest request) {
        TokensResponse tokensResponse = authService.refreshToken(request);
        return ResponseEntity.ok(tokensResponse);
    }


    @GetMapping("/public/fetchAccount")
    public ResponseEntity<LoginResponse> getUserInfo(HttpServletRequest request) {
        String accessToken = jwtUtils.getJwtFromHeader(request);
        if (accessToken == null || !jwtUtils.validateJwtToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Giải mã token để lấy thông tin người dùng
        String username = jwtUtils.getUserNameFromJwtToken(accessToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        // Tạo phản hồi
        LoginResponse loginResponse = new LoginResponse(userDetails.getUsername(),roles,userDetails.getEmail(), userDetails.getPhone());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/public/logout")
    public ResponseEntity<LogoutResponse> logoutUser(HttpServletRequest request) {
        LogoutResponse logoutResponse=authService.logout(request);
        // Gọi service để xử lý đăng xuất
        return ResponseEntity.ok(logoutResponse); // Trả về phản hồi thành công
    }

}
