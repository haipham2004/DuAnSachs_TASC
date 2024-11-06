package com.example.users_service.repository;

import com.example.users_service.dto.UsersDto;
import com.example.users_service.dto.response.UsersResponse;
import com.example.users_service.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {

    @Query("SELECT new com.example.users_service.dto.response.UsersResponse(u.userId,u.username, u.email, u.phone, r.enumRolesName) " +
            "FROM Users u LEFT JOIN Roles r ON u.idRoles = r.roleId where u.deletedAt=false ")
    List<UsersResponse> findAllUserDto();

    @Query("SELECT new com.example.users_service.dto.response.UsersResponse(u.userId,u.username, u.email, u.phone, r.enumRolesName) " +
            "FROM Users u LEFT JOIN Roles r ON u.idRoles = r.roleId where u.deletedAt=false")
    Page<UsersResponse> findAllUserDtoWithPage(Pageable pageable);

    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    @Query("SELECT u FROM Users u where u.deletedAt=false and u.idRoles=:id")
    Optional<UsersResponse> findByDeletedFalse(Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.deletedAt = true WHERE u.userId = :id and u.deletedAt=false ")
    void deleteSoft(Integer id);
}
