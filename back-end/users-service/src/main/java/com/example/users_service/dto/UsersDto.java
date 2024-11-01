package com.example.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UsersDto {

    private Long userId;

    private String userName;

    private String email;

    private boolean accountNonLocked;

    private boolean accountNonExpired;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private LocalDate credentialsExpiryDate;

    private LocalDate accountExpiryDate;

    private String twoFactorSecret;

    private boolean isTwoFactorEnabled;

    private String signUpMethod;

    private Integer idRoles;

    public UsersDto(String userName, String email, Integer idRoles) {
        this.userName = userName;
        this.email = email;
        this.idRoles = idRoles;
    }
}

