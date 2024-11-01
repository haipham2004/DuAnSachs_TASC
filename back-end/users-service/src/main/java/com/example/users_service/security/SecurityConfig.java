package com.example.users_service.security;


import com.example.users_service.entity.EnumRoles;
import com.example.users_service.entity.Roles;
import com.example.users_service.entity.Users;
import com.example.users_service.repository.RolesRepository;
import com.example.users_service.repository.UsersRepository;
import com.example.users_service.security.jwt.AuthEntryPointJwt;
import com.example.users_service.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.sql.Date;
import java.time.LocalDate;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {

    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public SecurityConfig(AuthEntryPointJwt unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/auth/public/**")
        );
        //http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((requests)
                -> requests
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/csrf-token").permitAll()
                .anyRequest().authenticated());
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(RolesRepository roleRepository,
                                      UsersRepository usersRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Roles userRole = roleRepository.findByEnumRolesName(EnumRoles.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Roles(EnumRoles.ROLE_USER)));

            Roles adminRole = roleRepository.findByEnumRolesName(EnumRoles.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Roles(EnumRoles.ROLE_ADMIN)));

            if (!usersRepository.existsByUsername("user1")) {
                Users user1 = new Users("user1", "user1@example.com",
                        passwordEncoder.encode("password1"));
                user1.setAccountNonLocked(false);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(Date.valueOf(LocalDate.now().plusYears(1)));
                user1.setAccountExpiryDate(Date.valueOf(LocalDate.now().plusYears(1)));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRoles(userRole);
                usersRepository.save(user1);
            }

            if (!usersRepository.existsByUsername("admin")) {
                Users admin = new Users("admin", "admin@example.com",
                        passwordEncoder.encode("adminPass"));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(Date.valueOf(LocalDate.now().plusYears(1)));
                admin.setAccountExpiryDate(Date.valueOf(LocalDate.now().plusYears(1)));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRoles(adminRole);
                usersRepository.save(admin);
            }
        };
    }
}


