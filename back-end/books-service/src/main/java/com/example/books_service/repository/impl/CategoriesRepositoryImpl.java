package com.example.books_service.repository.impl;

import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.mapper.CategoriesRowMapper;
import com.example.books_service.repository.CategoriesServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriesRepositoryImpl implements CategoriesServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoriesRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CategoriesRowMapper getCategoriesMapper(){
        return new CategoriesRowMapper();
    }

    @Override
    public List<CategoriesResponse> findAllCategoriesDto(){
        String sql="SELECT category_id,name_categories FROM categories";
        return jdbcTemplate.query(sql,getCategoriesMapper());
    }

}
