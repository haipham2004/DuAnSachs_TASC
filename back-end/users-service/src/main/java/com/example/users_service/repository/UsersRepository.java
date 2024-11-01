package com.example.users_service.repository;

import com.example.users_service.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

//    @Query("SELECT new com.example.users_service.dto.UsersDto(u.username,u.email,u.i) FROM Users u LEFT JOIN Roles r ON u.idRoles=r.roleId")
//    List<Users> findAllUserDto();

    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("SELECT us FROM Users us where us.deletedAt=false ")
    Page<Users> page(Pageable pageable);
}
