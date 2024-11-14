package com.example.users_service.dto;

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
public class UsersDto {

    private Integer userId;

    @NotBlank(message = "Username cannot be empty")  // Kiểm tra username không trống
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")  // Đảm bảo độ dài hợp lý
    private String username;

    @NotBlank(message = "Email cannot be empty")  // Kiểm tra email không trống
    @Email(message = "Invalid email format")  // Kiểm tra email hợp lệ
    private String email;

    @NotBlank(message = "Phone cannot be empty")  // Kiểm tra phone không trống
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String phone;

    //    @JsonIgnore
    private String password;

    //    @JsonIgnore
    private Integer idRoles;

    private String enumRolesName;


}


