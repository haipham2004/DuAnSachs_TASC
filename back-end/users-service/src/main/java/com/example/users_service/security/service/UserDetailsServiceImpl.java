package com.example.users_service.security.service;

import com.example.users_service.entity.Roles;
import com.example.users_service.entity.Users;
import com.example.users_service.repository.RolesRepository;
import com.example.users_service.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UsersRepository usersRepository;
    private RolesRepository rolesRepository;

    @Autowired
    public UserDetailsServiceImpl(UsersRepository usersRepository, RolesRepository rolesRepository) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        // Lấy vai trò của người dùng
        Roles role = rolesRepository.findById(user.getIdRoles())
                .orElseThrow(() -> new UsernameNotFoundException("Role Not Found for user: " + username));

        // Xây dựng đối tượng UserDetailsImpl
        return UserDetailsImpl.build(user, role);
    }
}



