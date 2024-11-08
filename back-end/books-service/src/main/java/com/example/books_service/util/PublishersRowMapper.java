package com.example.books_service.util;

import com.example.books_service.dto.response.PublishersResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PublishersRowMapper implements RowMapper<PublishersResponse> {
    @Override
    public PublishersResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        PublishersResponse publishersResponse=new PublishersResponse();
        publishersResponse.setName(rs.getString("name_publishers"));
        return publishersResponse;
    }
}
