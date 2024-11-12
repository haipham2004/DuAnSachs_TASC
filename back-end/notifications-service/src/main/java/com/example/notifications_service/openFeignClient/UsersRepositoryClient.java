package com.example.notifications_service.openFeignClient;

import com.example.notifications_service.dto.response.ApiResponse;
import com.example.notifications_service.dto.response.UsersResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("USERS-SERVICE")
public interface UsersRepositoryClient {

    @GetMapping(value = "/users/findAllUser", consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<UsersResponse>> findAll();
}
