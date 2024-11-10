package com.example.books_service.repository.impl;

import com.example.books_service.dto.response.PublishersResponse;
import com.example.books_service.mapper.PublishersRowMapper;
import com.example.books_service.repository.PublishersServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublishersRepositoryImpl implements PublishersServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PublishersRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PublishersRowMapper getPublisherMapper(){
        return new PublishersRowMapper();
    }

    @Override
    public List<PublishersResponse> findAllCategoriesDto(){
        String sql="SELECT publisher_id ,name_publishers FROM publishers";
        return jdbcTemplate.query(sql,getPublisherMapper());
    }

}
