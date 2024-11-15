package com.example.books_service.repository.impl;

import com.example.books_service.dto.request.PublishersRequest;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.dto.response.PublishersResponse;
import com.example.books_service.exception.CustomException;
import com.example.books_service.mapper.PublishersRowMapper;
import com.example.books_service.repository.PublishersServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PublishersRepositoryImpl implements PublishersServiceRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired
    public PublishersRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public PublishersRowMapper getPublisherMapper(){
        return new PublishersRowMapper();
    }


    @Override
    public PageResponse<PublishersResponse> findAll(String name, String phone, int pageNum, int pageSize) {
        // Calculate the offset for pagination
        int offset = (pageNum - 1) * pageSize;

        // Use :pageSize for LIMIT and :offset for pagination
        String sql = "SELECT publisher_id, name_publishers, address, phone, email, deleted_at " +
                "FROM publishers " +
                "WHERE (name_publishers LIKE :name OR :name IS NULL) " +
                "AND (phone LIKE :phone OR :phone IS NULL) " +
                "LIMIT :pageSize OFFSET :offset";

        Map<String, Object> params = new HashMap<>();
        params.put("name", name != null ? "%" + name + "%" : null);
        params.put("phone", phone != null ? "%" + phone + "%" : null);
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        List<PublishersResponse> publishers = namedParameterJdbcTemplate.query(
                sql, params, getPublisherMapper()
        );

        String countSql = "SELECT COUNT(*) FROM publishers " +
                "WHERE (name_publishers LIKE :name OR :name IS NULL) " +
                "AND (phone LIKE :phone OR :phone IS NULL)";

        int totalElements = namedParameterJdbcTemplate.queryForObject(countSql, params, Integer.class);

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageResponse<>(publishers, totalElements, totalPages, pageNum);
    }


    @Override
    public PublishersRequest save(PublishersRequest publishersRequest) {
        String sql = "INSERT INTO publishers (name_publishers, phone, address, email) " +
                "VALUES (:name, :phone, :address, :email)";

        Map<String, Object> params = new HashMap<>();
        params.put("name", publishersRequest.getName());
        params.put("phone", publishersRequest.getPhone());
        params.put("address", publishersRequest.getAddress());
        params.put("email", publishersRequest.getEmail());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
        publishersRequest.setPublisherId(keyHolder.getKey().intValue());
        return publishersRequest;
    }

    @Override
    public PublishersRequest update(Integer id, PublishersRequest publishersRequest) {
        String sql = "UPDATE publishers SET name_publishers = :name, phone = :phone, " +
                "address = :address, email = :email WHERE publisher_id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("name", publishersRequest.getName());
        params.put("phone", publishersRequest.getPhone());
        params.put("address", publishersRequest.getAddress());
        params.put("email", publishersRequest.getEmail());
        params.put("id", id);

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);

        if (rowsAffected > 0) {
            publishersRequest.setPublisherId(id);
            return publishersRequest;
        } else {
            throw new CustomException("Failed to update publisher with ID " + id);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "UPDATE publishers SET deleted_at = NOW() WHERE publisher_id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);

        if (rowsAffected == 0) {
            throw new CustomException("Failed to delete publisher with ID " + id);
        }
    }
}

