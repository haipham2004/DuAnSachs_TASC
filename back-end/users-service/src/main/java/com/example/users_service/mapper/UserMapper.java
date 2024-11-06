package com.example.users_service.mapper;

import com.example.users_service.dto.UsersDto;
import com.example.users_service.dto.request.UsersRequest;
import com.example.users_service.dto.response.UsersResponse;
import com.example.users_service.entity.Users;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel ="spring")
public interface UserMapper {

//    UsersDto mapToUsersDto(Users users);
//
//    Users mapToUsers(UsersDto usersDto);

    UsersResponse mapToUsersResponse(Users users);

    Users mapToUsers(UsersRequest usersRequest);

    UsersRequest mapToUsersRequest(Users users);

}
