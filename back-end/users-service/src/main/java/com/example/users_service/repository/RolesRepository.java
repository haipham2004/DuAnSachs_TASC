package com.example.users_service.repository;

import com.example.users_service.entity.EnumRoles;
import com.example.users_service.entity.Roles;
import com.example.users_service.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Integer> {

    Optional<Roles> findByEnumRolesName(EnumRoles enumRoles);

//    Optional<Roles> findByenumRolesName(String username);
}