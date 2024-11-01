package com.example.users_service.service.imp;

import com.example.users_service.entity.EnumRoles;
import com.example.users_service.entity.Roles;
import com.example.users_service.entity.Users;
import com.example.users_service.repository.RolesRepository;
import com.example.users_service.repository.UsersRepository;
import com.example.users_service.security.jwt.JwtUtils;
import com.example.users_service.security.request.LoginRequest;
import com.example.users_service.security.request.SignupRequest;
import com.example.users_service.security.response.LoginResponse;
import com.example.users_service.security.response.MessageResponse;
import com.example.users_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImp implements AuthService {

    private JwtUtils jwtUtils;

    private AuthenticationManager authenticationManager;

    private UsersRepository usersRepository;

    private RolesRepository rolesRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImp(JwtUtils jwtUtils, AuthenticationManager authenticationManager, UsersRepository usersRepository,
                    RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);

        }

        //  set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // specific to our implemetation
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        // Collect roles from the UserDetails
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Prepare the response body, now including the JWT token directly in the body
        LoginResponse response = new LoginResponse(userDetails.getUsername(),
                roles, jwtToken);
        return response;
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Tạo tài khoản người dùng mới
        Users user = new Users(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
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

        // Thiết lập thuộc tính cho người dùng
        user.setRoles(role);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setCredentialsExpiryDate(Date.valueOf(LocalDate.now().plusYears(1)));
        user.setAccountExpiryDate(Date.valueOf(LocalDate.now().plusYears(1)));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");

        // Lưu người dùng vào cơ sở dữ liệu
        usersRepository.save(user);

        // Trả về phản hồi đăng ký
        return new MessageResponse("User registered successfully!");
    }

}
