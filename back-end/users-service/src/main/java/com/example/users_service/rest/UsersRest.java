package com.example.users_service.rest;

import com.example.users_service.dto.request.UsersRequest;
import com.example.users_service.dto.response.ApiResponse;
import com.example.users_service.dto.response.UsersResponse;
import com.example.users_service.entity.Users;
import com.example.users_service.service.UsersService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("users")
public class UsersRest {

    private UsersService usersService;

    @Autowired
    public UsersRest(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("findAllUser")
    public ApiResponse<List<UsersResponse>> findAll() {
        return ApiResponse.<List<UsersResponse>>builder().results(usersService.findAll()).build();
    }

    @CrossOrigin
    @GetMapping("findAllUserWithPage")
    public ApiResponse<Page<UsersResponse>> findAllUserWithPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.<Page<UsersResponse>>builder().results(usersService.findAllUserDtoWithPage(pageable)).build();
    }

//    @GetMapping("findAllUserWithPage")
//    public ApiResponse<Page<UsersResponse>> findAllUserWithPage(
//            @Filter Specification<Users> specification,
//            Pageable pageable) {
//        return ApiResponse.<Page<UsersResponse>>builder().results(usersService.findAllUserDtoWithPage(specification, pageable)).build();
//    }


    @PostMapping("save")
    public ApiResponse<UsersRequest> save(@Valid @RequestBody UsersRequest usersRequest) {
        return ApiResponse.<UsersRequest>builder().message("Create Success").results(usersService.save(usersRequest))
                .build();
    }

    @PutMapping("update/{id}")
    public ApiResponse<UsersRequest> updatesave(@Valid @RequestBody UsersRequest usersRequest, @PathVariable("id") Integer id) {
        return ApiResponse.<UsersRequest>builder().message("Update Success").results(usersService.update(usersRequest, id))
                .build();
    }

    @DeleteMapping("deleteUser/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Integer id) {
        usersService.deleteById(id);
        return ApiResponse.<Void>builder().message("Deltee success: " + id).build();
    }
}
