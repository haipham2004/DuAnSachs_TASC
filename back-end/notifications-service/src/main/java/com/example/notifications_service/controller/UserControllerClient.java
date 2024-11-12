package com.example.notifications_service.controller;

import com.example.notifications_service.dto.response.ApiResponse;
import com.example.notifications_service.dto.response.UsersResponse;
import com.example.notifications_service.service.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("clientUser")
public class UserControllerClient {

    private UserServiceClient userServiceClient;

    @Autowired
    public UserControllerClient(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @GetMapping("findAll")
    public ApiResponse<List<UsersResponse>> findAll() {
        return userServiceClient.findAll();
    }

}
