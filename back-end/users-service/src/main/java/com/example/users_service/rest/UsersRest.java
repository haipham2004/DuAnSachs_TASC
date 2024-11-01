package com.example.users_service.rest;

import com.example.users_service.dto.UsersDto;
import com.example.users_service.service.UsersService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UsersRest {

    private UsersService usersService;

    @Autowired
    public UsersRest(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List<UsersDto>> findAll(){
        return ResponseEntity.ok(usersService.findAll());
    }

    @GetMapping("/page-users")
    public Page<UsersDto> gePagetUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usersService.page(pageable);
    }
}
