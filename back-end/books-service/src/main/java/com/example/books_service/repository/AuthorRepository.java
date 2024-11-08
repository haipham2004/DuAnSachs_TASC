package com.example.books_service.repository;

import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.util.AuthorsRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthorRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AuthorsRowMapper getAuthorsMapper(){
        return new AuthorsRowMapper();
    }

   public List<AuthorsResponse> findAllAuthorsDto(){
        String sql="SELECT name_authors FROM authors";
       return jdbcTemplate.query(sql,getAuthorsMapper());
    }
}
