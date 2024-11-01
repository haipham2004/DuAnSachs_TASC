package com.example.users_service.mapper;

import com.example.users_service.dto.UsersDto;
import com.example.users_service.entity.Users;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel ="spring")
public interface UserMapper {

    UsersDto mapToUsersDto(Users users);

    Users mapToUsers(UsersDto usersDto);
}
