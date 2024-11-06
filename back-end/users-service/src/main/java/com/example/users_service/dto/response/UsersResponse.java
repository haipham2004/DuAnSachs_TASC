package com.example.users_service.dto.response;

import com.example.users_service.entity.EnumRoles;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersResponse {

    private Integer userId;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone cannot be empty")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String phone;

    private Integer idRole;

    private String enumRolesName;

    // Constructor chuyển enum thành String
    public UsersResponse(Integer userId, String username, String email, String phone, EnumRoles enumRolesName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.enumRolesName = enumRolesName != null ? enumRolesName.name() : null;
    }
}
