package com.example.notifications_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    private String username;

    private String email;

    private String phone;

    private String fullName;

    private String addRess;

    private Integer idRole;

    private String enumRolesName;

    // Constructor chuyển enum thành String
//    public UsersResponse(Integer userId, String username, String email, String phone, EnumRoles enumRolesName) {
//        this.userId = userId;
//        this.username = username;
//        this.email = email;
//        this.phone = phone;
//        this.enumRolesName = enumRolesName != null ? enumRolesName.name() : null;
//    }

    public UsersResponse(Integer userId, String username, String email, String phone, String fullName, String addRess, EnumRoles enumRolesName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.fullName = fullName;
        this.addRess = addRess;
        this.enumRolesName = enumRolesName != null ? enumRolesName.name() : null;
    }
}
