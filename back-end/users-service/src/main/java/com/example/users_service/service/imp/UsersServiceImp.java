package com.example.users_service.service.imp;

import com.example.users_service.dto.UsersDto;
import com.example.users_service.entity.Users;
import com.example.users_service.exception.ResourceNotfound;
import com.example.users_service.mapper.UserMapper;
import com.example.users_service.repository.UsersRepository;
import com.example.users_service.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersServiceImp implements UsersService {

    private UsersRepository usersRepository;

    private UserMapper userMapper;

    @Autowired
    public UsersServiceImp(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UsersDto> findAll() {
        return usersRepository.findAll().stream()
                .map(users -> userMapper.mapToUsersDto(users)).collect(Collectors.toList());
    }

    @Override
    public List<UsersDto> findAllUserDto() {
//        return usersRepository.findAllUserDto().stream()
//                .map(users -> userMapper.mapToUsersDto(users)).collect(Collectors.toList());
        return null;
    }

    @Override
    public UsersDto getUserById(Integer id) {
        return userMapper.mapToUsersDto(usersRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Khong ton tai Usser co id: "+id)));
    }

    @Override
    public Page<UsersDto> page(Pageable pageable) {
        return usersRepository.findAll(pageable).map(users -> userMapper.mapToUsersDto(users));
    }
}
