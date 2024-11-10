package com.example.books_service.mapper;

import com.example.books_service.dto.response.BooksResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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
        booksResponse.setConsPrice(rs.getDouble("cost_price"));
        booksResponse.setQuantity(rs.getInt("quantity"));
        booksResponse.setStatus(rs.getString("status"));
        String imageUrlsString = rs.getString("image_url");

        if (imageUrlsString != null && !imageUrlsString.isEmpty()) {
            List<String> imageUrls = Arrays.asList(imageUrlsString.split(","));
            booksResponse.setImageUrl(imageUrls);
        }
        booksResponse.setThumbnail(rs.getString("thumbnail"));
        booksResponse.setAuthorId(rs.getInt("author_id"));
        booksResponse.setPublisherId(rs.getInt("publisher_id"));
        booksResponse.setCategoryId(rs.getInt("category_id"));
        return booksResponse;
    }
}