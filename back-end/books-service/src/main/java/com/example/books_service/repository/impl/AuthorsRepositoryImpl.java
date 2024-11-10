package com.example.books_service.repository.impl;

import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.mapper.AuthorsRowMapper;
import com.example.books_service.repository.AuthorsServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorsRepositoryImpl implements AuthorsServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorsRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AuthorsRowMapper getAuthorsMapper(){
        return new AuthorsRowMapper();
    }

    @Override
   public List<AuthorsResponse> findAllAuthorsDto(){
        String sql="SELECT author_id ,name_authors FROM authors";
       return jdbcTemplate.query(sql,getAuthorsMapper());
    }
}
