package com.example.books_service.repository.impl;

import com.example.books_service.dto.request.AuthorsRequest;
import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.dto.response.PageResponse;
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
    public PageResponse<AuthorsResponse> findAllAuthorsDto( String name, String phone, int pageNum, int pageSize) {

        int offset = (pageNum - 1) * pageSize;

        String sql = "SELECT author_id, name_authors, phone, address, deleted_at " +
                "FROM authors " +
                "WHERE (name_authors LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
                "AND (phone LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
                "LIMIT ? OFFSET ?";

        List<AuthorsResponse> authors = jdbcTemplate.query(sql, getAuthorsMapper(), name, name, phone, phone, pageSize, offset);


        String countSql = "SELECT COUNT(*) FROM authors " +
                "WHERE (name_authors LIKE CONCAT('%', ?, '%') OR ? IS NULL) " +
                "AND (phone LIKE CONCAT('%', ?, '%') OR ? IS NULL)";

        int totalElements = jdbcTemplate.queryForObject(countSql, Integer.class, name, name, phone, phone);

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageResponse<>(authors, totalElements, totalPages, pageNum);
    }

    @Override
    public AuthorsRequest save(AuthorsRequest authorsRequest) {
        String sql="INSERT INTO authors(name_authors, phone, address) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, authorsRequest.getName());
            ps.setString(2, authorsRequest.getPhone());
            ps.setString(3, authorsRequest.getAddRess());
            return ps;
        }, keyHolder);
        authorsRequest.setAuthorId(keyHolder.getKey().intValue());
        return authorsRequest;
    }

    @Override
    public AuthorsRequest update(Integer id, AuthorsRequest authorsRequest) {
        String sql = "UPDATE authors SET name_authors = ?, phone=?, address=?  WHERE author_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, authorsRequest.getName(), authorsRequest.getPhone(), authorsRequest.getAddRess(), id);
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
