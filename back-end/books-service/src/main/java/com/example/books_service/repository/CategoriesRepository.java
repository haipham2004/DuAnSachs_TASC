package com.example.books_service.repository;

import com.example.books_service.dto.response.AuthorsResponse;
import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.util.AuthorsRowMapper;
import com.example.books_service.util.CategoriesRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriesRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoriesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CategoriesRowMapper getCategoriesMapper(){
        return new CategoriesRowMapper();
    }

    public List<CategoriesResponse> findAllCategoriesDto(){
        String sql="SELECT name_categories FROM categories";
        return jdbcTemplate.query(sql,getCategoriesMapper());
    }

}
