package com.example.books_service.mapper;

import com.example.books_service.dto.response.AuthorsResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorsRowMapper implements RowMapper<AuthorsResponse> {
    @Override
    public AuthorsResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorsResponse authorsResponse=new AuthorsResponse();
        authorsResponse.setId(rs.getInt("author_id"));
        authorsResponse.setName(rs.getString("name_authors"));
        authorsResponse.setDeletedAt(rs.getBoolean("deleted_at"));
        return authorsResponse;
    }
}
