package com.example.books_service.util;

import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.dto.response.CategoriesResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriesRowMapper implements RowMapper<CategoriesResponse> {
    @Override
    public CategoriesResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        CategoriesResponse categoriesResponse=new CategoriesResponse();
        categoriesResponse.setName(rs.getString("name_categories"));
        return categoriesResponse;
    }
}