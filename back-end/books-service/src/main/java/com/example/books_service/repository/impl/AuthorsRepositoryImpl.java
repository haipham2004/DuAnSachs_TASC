package com.example.books_service.repository.impl;

import com.example.books_service.dto.request.AuthorsRequest;
import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.exception.CustomException;
import com.example.books_service.exception.MessageExceptionResponse;
import com.example.books_service.mapper.AuthorsRowMapper;
import com.example.books_service.repository.AuthorsServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
        String sql="SELECT author_id ,name_authors,deleted_at FROM authors";
       return jdbcTemplate.query(sql,getAuthorsMapper());
    }

    @Override
    public AuthorsRequest save(AuthorsRequest authorsRequest) {
        String sql="INSERT INTO authors(name_authors) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, authorsRequest.getName());
            return ps;
        }, keyHolder);
        authorsRequest.setAuthorId(keyHolder.getKey().intValue());
        return authorsRequest;
    }

    @Override
    public AuthorsRequest update(Integer id, AuthorsRequest authorsRequest) {
        String sql = "UPDATE authors SET name_authors = ? WHERE author_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, authorsRequest.getName(), id);


        if (rowsAffected > 0) {
            authorsRequest.setAuthorId(id);
            return authorsRequest;
        } else {
             throw new CustomException(MessageExceptionResponse.update_author_errol);
        }
    }

    @Override
    public void delete(Integer id) {

    }


}
