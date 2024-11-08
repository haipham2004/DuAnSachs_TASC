package com.example.books_service.repository;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.exception.ResourceNotfound;
import com.example.books_service.util.BooksRowMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class BooksRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BooksRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private BooksRowMapper getBookMapper() {
        return new BooksRowMapper();
    }


    public List<BooksResponse> findAllBooksDto() {
        String sql = "SELECT b.book_id, b.title, " +
                "a.name_authors, " +
                "p.name_publishers , " +
                "c.name_categories, " +
                "b.price, b.description, b.stock, b.quantity, b.status, b.image_url, b.thumbnail  " +
                "FROM books b " +
                "LEFT JOIN authors a ON b.author_id = a.author_id " +
                "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
                "LEFT JOIN categories c ON b.category_id = c.category_id";

        return jdbcTemplate.query(sql, getBookMapper());
    }

    public BooksResponse findById(Integer id) {
        String sql = "SELECT b.book_id, b.title, " +
                "a.name_authors, " +
                "p.name_publishers , " +
                "c.name_categories, " +
                "b.price, b.description, b.stock, b.quantity, b.status,b.image_url, b.thumbnail " +
                "FROM books b " +
                "LEFT JOIN authors a ON b.author_id = a.author_id " +
                "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
                "LEFT JOIN categories c ON b.category_id = c.category_id where b.book_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, getBookMapper(), id);
        } catch (EmptyResultDataAccessException e) {

            throw new ResourceNotfound("Book with ID " + id + " not found.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error accessing database", e);
        }
    }


    public BooksRequest save(BooksRequest booksRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        String imageUrlJson = null;
        try {
            imageUrlJson = objectMapper.writeValueAsString(booksRequest.getImageUrl());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String sql = "INSERT INTO books (title, author_id, publisher_id, category_id, price, description, stock, quantity, status, image_url,thumbnail) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, booksRequest.getTitle(), booksRequest.getAuthorId(), booksRequest.getPublisherId(),
                booksRequest.getCategoryId(), booksRequest.getPrice(), booksRequest.getDescription(),
                booksRequest.getStock(), booksRequest.getQuantity(), booksRequest.getStatus(), imageUrlJson,booksRequest.getThumbnail());
        String getLastIdSql = "SELECT LAST_INSERT_ID()";
        Integer newBookId = jdbcTemplate.queryForObject(getLastIdSql, Integer.class);
        booksRequest.setBookId(newBookId);
        return booksRequest;
    }


    public BooksRequest update(BooksRequest booksRequest, Integer id) {
        String imageUrlStr = String.join(",", booksRequest.getImageUrl());
        String sql = "UPDATE books SET title = ?, author_id = ?, publisher_id = ?, category_id = ?, price = ?, " +
                "description = ?, stock = ?, quantity = ?, status = ?, image_url=?, thumbnail=?  WHERE book_id = ?";
        jdbcTemplate.update(sql, booksRequest.getTitle(), booksRequest.getAuthorId(), booksRequest.getPublisherId(),
                booksRequest.getCategoryId(), booksRequest.getPrice(), booksRequest.getDescription(),
                booksRequest.getStock(), booksRequest.getQuantity(), booksRequest.getStatus(), imageUrlStr, booksRequest.getThumbnail(), id);
        booksRequest.setBookId(id);
        return booksRequest;
    }


    public void deleteById(Integer id) {
        String sql = "DELETE FROM books WHERE book_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public PageResponse<BooksResponse> findBooksPage( int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT b.book_id, b.title, " +
                "a.name_authors, " +
                "p.name_publishers, " +
                "c.name_categories, " +
                "b.price, b.description, b.stock, b.quantity, b.status, b.image_url, b.thumbnail " +
                "FROM books b " +
                "LEFT JOIN authors a ON b.author_id = a.author_id " +
                "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
                "LEFT JOIN categories c ON b.category_id = c.category_id " +
                "LIMIT ? OFFSET ?";

        List<BooksResponse> books = jdbcTemplate.query(sql, getBookMapper(), pageSize, offset);

        String countSql = "SELECT COUNT(*) FROM books";

        int totalElements = jdbcTemplate.queryForObject(countSql, Integer.class);

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageResponse<>(books, totalElements, totalPages, pageNumber);
    }

}
