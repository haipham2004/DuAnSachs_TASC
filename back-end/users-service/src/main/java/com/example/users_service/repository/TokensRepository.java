package com.example.users_service.repository;

import com.example.users_service.entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokensRepository extends JpaRepository<Tokens, Integer> {

    Optional<Tokens> findByToken(String token);
//
//    Optional<Tokens> findByToken(Integer token);

    @Query("SELECT t FROM Tokens t WHERE t.idUsers = :userId")
    Optional<Tokens> findByIdUsers(Integer userId);



}
