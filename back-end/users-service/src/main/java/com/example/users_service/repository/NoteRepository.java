package com.example.users_service.repository;

import com.example.users_service.entity.Note;
import com.example.users_service.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Integer> {
    List<Note> findByOwnerUsername(String ownerUsername);
}
