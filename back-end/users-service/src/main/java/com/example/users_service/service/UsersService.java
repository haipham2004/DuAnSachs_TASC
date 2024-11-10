package com.example.users_service.service;

import com.example.users_service.dto.request.UsersRequest;
import com.example.users_service.dto.response.UsersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsersService {

    List<UsersResponse> findAll();

    List<UsersResponse> findAllUserDto();

    Page<UsersResponse> findAllUserDtoWithPage(Pageable pageable);

//    Page<UsersResponse> findAllUserDtoWithPage(Specification<Users> specification, Pageable pageable);

    UsersResponse getUserById(Integer id);

    UsersResponse findById(Integer id);

    UsersRequest save(UsersRequest usersRequest);

    UsersRequest update(UsersRequest usersRequest, Integer id);

    void deleteById(Integer id);

}
