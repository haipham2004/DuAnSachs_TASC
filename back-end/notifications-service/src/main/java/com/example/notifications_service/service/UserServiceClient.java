package com.example.notifications_service.service;

import com.example.notifications_service.dto.response.ApiResponse;
import com.example.notifications_service.dto.response.UsersResponse;
import com.example.notifications_service.openFeignClient.UsersRepositoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceClient {

    private  UsersRepositoryClient usersRepositoryClient;

    @Autowired
    public UserServiceClient(UsersRepositoryClient usersRepositoryClient) {
        this.usersRepositoryClient = usersRepositoryClient;
    }

  public ApiResponse<List<UsersResponse>> findAll() {
        return usersRepositoryClient.findAll();  // Gọi Feign Client để lấy danh sách người dùng
    }
}
