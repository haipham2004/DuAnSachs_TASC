package com.example.users_service.service;

import com.example.users_service.dto.UsersDto;
import com.example.users_service.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsersService {

    List<UsersDto> findAll();

    List<UsersDto> findAllUserDto();

    UsersDto getUserById(Integer id);

    Page<UsersDto> page(Pageable pageable);
}
