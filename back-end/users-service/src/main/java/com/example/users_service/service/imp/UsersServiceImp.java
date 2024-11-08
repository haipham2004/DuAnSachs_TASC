package com.example.users_service.service.imp;

import com.example.users_service.dto.request.UsersRequest;
import com.example.users_service.dto.response.UsersResponse;
import com.example.users_service.entity.Roles;
import com.example.users_service.entity.Users;
import com.example.users_service.exception.CustomException;
import com.example.users_service.exception.MessageExceptionResponse;
import com.example.users_service.exception.ResourceNotfound;
import com.example.users_service.mapper.UserMapper;
import com.example.users_service.repository.RolesRepository;
import com.example.users_service.repository.UsersRepository;
import com.example.users_service.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersServiceImp implements UsersService {

    private UsersRepository usersRepository;

    private UserMapper userMapper;

    private PasswordEncoder passwordEncoder;

    private RolesRepository rolesRepository;

    @Autowired
    public UsersServiceImp(UsersRepository usersRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public List<UsersResponse> findAll() {
        return usersRepository.findAll().stream()
                .map(users -> userMapper.mapToUsersResponse(users)).collect(Collectors.toList());
    }

    @Override
    public List<UsersResponse> findAllUserDto() {
        return usersRepository.findAllUserDto();
    }

    @Override
    public Page<UsersResponse> findAllUserDtoWithPage(Pageable pageable) {
        return usersRepository.findAllUserDtoWithPage(pageable);
    }



//    @Override
//    public Page<UsersResponse> findAllUserDtoWithPage(Specification<Users> specification, Pageable pageable) {
//
//        Page<Users> usersPage = usersRepository.findAll(specification, pageable);
//
//

//        List<UsersResponse> usersResponses = usersPage.getContent().stream()
//                .map(user ->userMapper.mapToUsersResponse(user))
//                .collect(Collectors.toList());
//        return new PageImpl<>(usersResponses, pageable, usersPage.getTotalElements());
//    }

    @Override
    public UsersResponse getUserById(Integer id) {
        return userMapper.mapToUsersResponse(usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfound("Khong ton tai Usser co id: "+id)));
    }

    @Override
    public UsersResponse findById(Integer id) {
        return userMapper.mapToUsersResponse(usersRepository.findById(id).
                orElseThrow(() -> new ResourceNotfound("Không tìm thấy Users có ID: "+id)));
    }

    @Override
    public UsersRequest save(UsersRequest usersRequest) {
        if (usersRepository.existsByUsername(usersRequest.getUsername())) {
            throw new CustomException(MessageExceptionResponse.USERNAME_ALREADY_IN_USE);
        }

        if (usersRepository.existsByEmail(usersRequest.getEmail())) {
            throw new CustomException(MessageExceptionResponse.EMAIL_ALREADY_IN_USE);
        }

        if (usersRepository.existsByPhone(usersRequest.getPhone())) {
            throw new CustomException(MessageExceptionResponse.PHONE_ALREADY_IN_USE);
        }

        Roles roles = rolesRepository.findById(usersRequest.getIdRoles())
                .orElseThrow(() -> new ResourceNotfound("Role not found for id: " + usersRequest.getIdRoles()));

        Users users = userMapper.mapToUsers(usersRequest);
        String encodedPassword = passwordEncoder.encode(usersRequest.getPassword());
        users.setPassword(encodedPassword);
        users.setDeletedAt(false);
        Users userSave = usersRepository.save(users);
        return userMapper.mapToUsersRequest(userSave);
    }

    @Override
    public UsersRequest update(UsersRequest usersRequest, Integer id) {
        Users existsUsers = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfound("Không tìm thấy Users có ID: " + id));

        // Kiểm tra và cập nhật các trường khác
        if (!usersRequest.getUsername().equals(existsUsers.getUsername()) && usersRepository.existsByUsername(usersRequest.getUsername())) {
            throw new CustomException(MessageExceptionResponse.USERNAME_ALREADY_IN_USE);
        }

        if (!usersRequest.getEmail().equals(existsUsers.getEmail()) && usersRepository.existsByEmail(usersRequest.getEmail())) {
            throw new CustomException(MessageExceptionResponse.EMAIL_ALREADY_IN_USE);
        }

        if (!usersRequest.getPhone().equals(existsUsers.getPhone()) && usersRepository.existsByPhone(usersRequest.getPhone())) {
            throw new CustomException(MessageExceptionResponse.PHONE_ALREADY_IN_USE);
        }

        // Kiểm tra và cập nhật mật khẩu chỉ khi có mật khẩu mới
        if (usersRequest.getPassword() != null && !usersRequest.getPassword().isEmpty()) {
            // Nếu có mật khẩu mới, mã hóa và lưu mật khẩu
            existsUsers.setPassword(passwordEncoder.encode(usersRequest.getPassword()));
        } else {
            // Nếu không có mật khẩu mới, giữ nguyên mật khẩu cũ
            existsUsers.setPassword(existsUsers.getPassword());
        }

        // Cập nhật các trường khác
        existsUsers.setUsername(usersRequest.getUsername());
        existsUsers.setEmail(usersRequest.getEmail());
        existsUsers.setPhone(usersRequest.getPhone());
        existsUsers.setIdRoles(usersRequest.getIdRoles());
        existsUsers.setDeletedAt(false);  // Đảm bảo người dùng không bị xóa

        // Lưu người dùng cập nhật vào DB
        Users updatedUser = usersRepository.save(existsUsers);

        // Map entity sang request object và trả về
        return userMapper.mapToUsersRequest(updatedUser);
    }


    @Override
    public void deleteById(Integer id) {
        Users exitsUsers=usersRepository.findById(id).
                orElseThrow(() -> new ResourceNotfound("Không tìm thấy Users có ID: "+id));
        usersRepository.deleteSoft(id);
    }

}
