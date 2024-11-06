package com.example.users_service.service.imp;

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
import com.example.users_service.dto.request.LoginRequest;
import com.example.users_service.dto.request.SignupRequest;
import com.example.users_service.dto.response.LoginResponse;
import com.example.users_service.dto.response.LogoutResponse;
import com.example.users_service.dto.response.MessageResponse;
import com.example.users_service.dto.response.TokensResponse;
import com.example.users_service.security.service.UserDetailsImpl;
import com.example.users_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
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
public class AuthServiceImp implements AuthService {

    private JwtUtils jwtUtils;

    private AuthenticationManager authenticationManager;

    private UsersRepository usersRepository;

    private RolesRepository rolesRepository;

    private TokensRepository tokensRepository;

    private PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;

    @Autowired
    public AuthServiceImp(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UsersRepository usersRepository, RolesRepository rolesRepository, TokensRepository tokensRepository, PasswordEncoder passwordEncoder,
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
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        // Kiểm tra hợp lệ cho tên đăng nhập và mật khẩu
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

        LoginResponse response = new LoginResponse(jwtToken,jwtTokenRefresh, userDetails.getUsername(), roles, "Login token success",
                userDetails.getEmail(), userDetails.getPhone());
        return response;
    }


    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {

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
        return new MessageResponse("User registered successfully!", user.getUserId());
    }

    @Override
    public TokensResponse refreshToken(HttpServletRequest request) {

        String refreshToken = jwtUtils.getJwtFromHeader(request);


        if (refreshToken == null || !jwtUtils.validateJwtToken(refreshToken)) {
            throw new CustomException(MessageExceptionResponse.TOKEN_NOT_FOUND);
        }

        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

        Tokens existingToken = tokensRepository.findByIdUsers(userDetails.getId())
                .orElseThrow(() -> new CustomException(MessageExceptionResponse.TOKEN_NOT_FOUND));

        String newAccessToken = jwtUtils.generateTokenFromUsername(userDetails);

        String currentRefreshToken = existingToken.getToken();

        return new TokensResponse(newAccessToken, currentRefreshToken);
    }

    @Override
    public LogoutResponse logout(HttpServletRequest request) {
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

        return new LogoutResponse("User logged out successfully.");
    }


//    @Override
//    public TokensResponse refreshToken(HttpServletRequest request) {
//        // Lấy refresh token từ header
//        String refreshToken = jwtUtils.getJwtFromHeader(request);
//
//        // Kiểm tra tính hợp lệ của refresh token
//        if (refreshToken == null || !jwtUtils.validateJwtToken(refreshToken)) {
//            throw new CustomException(MessageExceptionResponse.TOKEN_NOT_FOUND);
//        }
//
//        // Lấy tên người dùng từ refresh token
//        String username = jwtUtils.getUserNameFromJwtToken(refreshToken);
//
//        // Tìm người dùng trong cơ sở dữ liệu
//        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
//
//        // Kiểm tra sự tồn tại của token trong cơ sở dữ liệu
//        Tokens existingToken = tokensRepository.findByIdUsers(userDetails.getId())
//                .orElseThrow(() -> new CustomException(MessageExceptionResponse.TOKEN_NOT_FOUND));
//
//        // Tạo token mới
//        String newAccessToken = jwtUtils.generateTokenFromUsername(userDetails);
//        String newRefreshToken = jwtUtils.generateTokenFromUsernameRefresh(userDetails);
//
//        // Cập nhật refresh token vào cơ sở dữ liệu
//        updateUserToken(newRefreshToken, userDetails.getId());
//
//        // Trả về token mới
//        return new TokensResponse(newAccessToken, newRefreshToken);
//    }
//
//    // Phương thức cập nhật token không cần thay đổi
//    private void updateUserToken(String refreshToken, Integer userId) {
//        Tokens token = tokensRepository.findByIdUsers(userId)
//                .orElseThrow(() -> new RuntimeException("Error: Token not found."));
//
//        token.setToken(refreshToken);
//        token.setExpirationDate(LocalDateTime.now().plus(jwtExpirationMsRefresh, ChronoUnit.MILLIS)); // Cập nhật thời gian hết hạn
//
//        tokensRepository.save(token);
//    }
}
