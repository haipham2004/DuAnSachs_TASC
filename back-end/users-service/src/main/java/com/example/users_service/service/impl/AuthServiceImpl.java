package com.example.users_service.service.impl;

import com.example.users_service.dto.request.LoginRequest;
import com.example.users_service.dto.request.SignupRequest;
import com.example.users_service.dto.request.TokenRequest;
import com.example.users_service.dto.response.LoginResponse;
import com.example.users_service.entity.EnumRoles;
import com.example.users_service.entity.Roles;
import com.example.users_service.entity.Tokens;
import com.example.users_service.entity.Users;
import com.example.users_service.exception.CustomException;
import com.example.users_service.exception.MessageExceptionResponse;
import com.example.users_service.repository.RolesRepository;
import com.example.users_service.repository.TokensRepository;
import com.example.users_service.repository.UsersRepository;
import com.example.users_service.security.jwt.JwtUtils;
import com.example.users_service.security.service.UserDetailsImpl;
import com.example.users_service.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@CrossOrigin("http://localhost:3000")
public class AuthServiceImpl implements AuthService {

    private JwtUtils jwtUtils;

    private AuthenticationManager authenticationManager;

    private UsersRepository usersRepository;

    private RolesRepository rolesRepository;

    private TokensRepository tokensRepository;

    private PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;

    @Autowired
    public AuthServiceImpl(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UsersRepository usersRepository, RolesRepository rolesRepository, TokensRepository tokensRepository, PasswordEncoder passwordEncoder,
                           UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.tokensRepository = tokensRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Value("${spring.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${spring.app.jwtExpirationMsRefresh}")
    private long jwtExpirationMsRefresh;

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest, HttpServletResponse response) {

        if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty()) {
            throw new CustomException(MessageExceptionResponse.USERNAME_ALREADY_IN_USE);
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            throw new CustomException(MessageExceptionResponse.PASSWORD_TOO_WEAK);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (AuthenticationException exception) {

            throw new CustomException(MessageExceptionResponse.INVALID_CREDENTIALS);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Tokens existingToken = tokensRepository.findByIdUsers(userDetails.getId()).orElse(null);
        if (existingToken != null) {
            tokensRepository.delete(existingToken);
        }

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        String jwtTokenRefresh = jwtUtils.generateTokenFromUsernameRefresh(userDetails);

        Tokens token = new Tokens();
        token.setToken(jwtTokenRefresh);
        token.setTokenType("refresh_token");
        LocalDateTime expirationDate = LocalDateTime.now().plus(jwtExpirationMsRefresh, ChronoUnit.MILLIS);
        token.setExpirationDate(expirationDate);
        token.setIdUsers(userDetails.getId());

        tokensRepository.save(token);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", jwtTokenRefresh)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtExpirationMsRefresh)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, resCookies.toString());

        LoginResponse loginResponse = new LoginResponse(jwtToken,jwtTokenRefresh, userDetails.getUsername(), roles, "Login token success",
                userDetails.getEmail(), userDetails.getPhone());
        return loginResponse;
    }

    @Override
    public void registerUser(SignupRequest signUpRequest) {
        if (usersRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new CustomException(MessageExceptionResponse.USERNAME_ALREADY_IN_USE);
        }

        if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException(MessageExceptionResponse.EMAIL_ALREADY_IN_USE);
        }

        if (usersRepository.existsByPhone(signUpRequest.getPhone())) {
            throw new CustomException(MessageExceptionResponse.PHONE_ALREADY_IN_USE);
        }

        Users user = new Users(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPhone(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        Set<String> strRoles = signUpRequest.getRole();
        Roles role;

        if (strRoles == null || strRoles.isEmpty()) {
            role = rolesRepository.findByEnumRolesName(EnumRoles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            String roleStr = strRoles.iterator().next();
            if ("ADMIN".equals(roleStr)) {
                role = rolesRepository.findByEnumRolesName(EnumRoles.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            } else {
                role = rolesRepository.findByEnumRolesName(EnumRoles.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            }
        }
        user.setIdRoles(role.getRoleId());
        usersRepository.save(user);
    }

    @Override
    public LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        // Lấy refresh token từ cookie
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        // Kiểm tra xem có refresh token hay không
        if (refreshToken == null) {
            throw new CustomException(MessageExceptionResponse.REFRESH_TOKEN_NOT_FOUND);
        }

        // Kiểm tra tính hợp lệ của refresh token
        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new CustomException(MessageExceptionResponse.INVALID_REFRESH_TOKEN);
        }

        // Lấy thông tin người dùng từ refresh token (subject là email hoặc username tùy vào cách mã hóa)
        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        // Kiểm tra xem người dùng có tồn tại trong hệ thống không
        if (userDetails == null) {
            throw new CustomException(MessageExceptionResponse.USER_NOT_FOUND);
        }

        // Tạo access token mới (không thay đổi refresh token)
        String newAccessToken = jwtUtils.generateTokenFromUsername(userDetails);

        // Không thay đổi refresh token, giữ nguyên refresh token hiện tại
        String currentRefreshToken = refreshToken;  // Giữ nguyên refresh token cũ

        // Lấy quyền của người dùng
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Thiết lập cookie mới cho refresh token (giữ nguyên refresh token cũ)
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", currentRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtExpirationMsRefresh)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, resCookies.toString());  // Đặt cookie trong header response

        // Tạo và trả về response với access token mới
        LoginResponse loginResponse = new LoginResponse(
                newAccessToken,    // Access token mới
                currentRefreshToken,   // Giữ nguyên refresh token cũ
                userDetails.getUsername(),  // Tên người dùng
                roles,             // Quyền của người dùng
                "Refresh token success",  // Thông báo thành công
                userDetails.getEmail(),  // Email người dùng
                userDetails.getPhone()   // Số điện thoại người dùng
        );

        return loginResponse;  // Trả về login response với access token mới
    }



    @Override
    public LoginResponse fetchAccount(HttpServletRequest request) {
        String accessToken = jwtUtils.getJwtFromHeader(request);
        if (accessToken == null || !jwtUtils.validateJwtToken(accessToken)) {
            throw new CustomException(MessageExceptionResponse.UNAUTHORIZED);
        }
        String username = jwtUtils.getUserNameFromJwtToken(accessToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        LoginResponse loginResponse = new LoginResponse(userDetails.getUsername(), roles, userDetails.getEmail(), userDetails.getPhone());
        return loginResponse;
    }

    @Override
    public void logout(HttpServletRequest request) {
        String accessToken = jwtUtils.getJwtFromHeader(request);

        if (accessToken == null || !jwtUtils.validateJwtToken(accessToken)) {
            throw new CustomException(MessageExceptionResponse.TOKEN_NOT_FOUND);
        }

        String username = jwtUtils.getUserNameFromJwtToken(accessToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        Tokens existingToken = tokensRepository.findByIdUsers(userDetails.getId())
                .orElseThrow(() -> new CustomException(MessageExceptionResponse.TOKEN_NOT_FOUND));

        existingToken.setToken(null);
        existingToken.setExpirationDate(LocalDateTime.now());

        tokensRepository.save(existingToken);

        SecurityContextHolder.clearContext();

    }


    @Override
    public LoginResponse checkToken(TokenRequest tokenRequest) {

        String accessToken = tokenRequest.getToken();

        if (accessToken == null || accessToken.trim().isEmpty() || !jwtUtils.validateJwtToken(accessToken)) {
            throw new CustomException(MessageExceptionResponse.UNAUTHORIZED);
        }

        String username = jwtUtils.getUserNameFromJwtToken(accessToken);

        UserDetailsImpl userDetails;
        try {
            userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new CustomException(MessageExceptionResponse.USER_NOT_FOUND);
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new LoginResponse(userDetails.getUsername(), roles, userDetails.getEmail(), userDetails.getPhone());
    }





}
