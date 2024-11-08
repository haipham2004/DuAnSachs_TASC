package com.example.books_service.service.imp;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.BooksResponse2;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class BooksRowMapper2 implements RowMapper<BooksResponse2> {
    @Override
    public BooksResponse2 mapRow(ResultSet rs, int rowNum) throws SQLException {
        BooksResponse2 booksResponse = new BooksResponse2();

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
        // Lấy chuỗi 'image_url' từ cơ sở dữ liệu
        String imageUrlsString = rs.getString("image_url");

        // Nếu chuỗi không null và không rỗng, tách chuỗi thành danh sách các URL
        if (imageUrlsString != null && !imageUrlsString.isEmpty()) {
            // Tách chuỗi thành danh sách các URL
            List<String> imageUrls = Arrays.asList(imageUrlsString.split(","));
            booksResponse.setImageUrl(imageUrls);
        }
        booksResponse.setThumbnail(rs.getString("thumbnail"));
        return booksResponse;
    }
}