package com.example.users_service.rest;


import com.example.users_service.dto.UsersDto;
import com.example.users_service.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminRest {

    UsersService userService;

    @Autowired
    public AdminRest(UsersService userService) {
        this.userService = userService;
    }

    @GetMapping("/getusers")
    public ResponseEntity<List<UsersDto>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(),
                HttpStatus.OK);
    }
//
//    @PutMapping("/update-role")
//    public ResponseEntity<String> updateUserRole(@RequestParam Long userId,
//                                                 @RequestParam String roleName) {
//        userService.updateUserRole(userId, roleName);
//        return ResponseEntity.ok("User role updated");
//    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UsersDto> getUser(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id),
                HttpStatus.OK);
    }


}

