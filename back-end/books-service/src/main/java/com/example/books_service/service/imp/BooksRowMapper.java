package com.example.books_service.service.imp;

import com.example.books_service.dto.response.BooksResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BooksRowMapper implements RowMapper<BooksResponse> {
    @Override
    public BooksResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        BooksResponse booksResponse = new BooksResponse();

        booksResponse.setBookId(rs.getInt("book_id"));
        booksResponse.setTitle(rs.getString("title"));
        booksResponse.setNameAuthor(rs.getString("name_authors"));
        booksResponse.setNamePublisher(rs.getString("name_publishers"));
        booksResponse.setNameCategory(rs.getString("name_categories"));
        booksResponse.setPrice(rs.getDouble("price"));
        booksResponse.setDescription(rs.getString("description"));
        booksResponse.setStock(rs.getInt("stock"));
        booksResponse.setQuantity(rs.getInt("quantity"));
        booksResponse.setStatus(rs.getString("status"));
        booksResponse.setImageUrl(rs.getString("image_url"));
        booksResponse.setThumbnail(rs.getString("thumbnail"));
        return booksResponse;
    }
}