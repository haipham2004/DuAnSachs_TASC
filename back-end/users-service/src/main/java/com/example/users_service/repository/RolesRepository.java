package com.example.users_service.repository;

import com.example.users_service.entity.EnumRoles;
import com.example.users_service.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Integer> {

    Optional<Roles> findByEnumRolesName(EnumRoles enumRoles);

}