package com.example.books_service.repository;

import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.dto.response.PublishersResponse;
import com.example.books_service.util.CategoriesRowMapper;
import com.example.books_service.util.PublishersRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublishersRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PublishersRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PublishersRowMapper getPublisherMapper(){
        return new PublishersRowMapper();
    }

    public List<PublishersResponse> findAllCategoriesDto(){
        String sql="SELECT name_publishers FROM publishers";
        return jdbcTemplate.query(sql,getPublisherMapper());
    }

}
