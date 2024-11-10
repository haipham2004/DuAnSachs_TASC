package com.example.books_service.repository.impl;

import com.example.books_service.dto.request.BooksRequest;
import com.example.books_service.dto.response.BooksResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.exception.ResourceNotfound;
import com.example.books_service.mapper.BooksRowMapper;
import com.example.books_service.repository.BooksServiceRepository;
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
public class BooksRepositoryImpl implements BooksServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BooksRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private BooksRowMapper getBookMapper() {
        return new BooksRowMapper();
    }

    @Override
    public List<BooksResponse> findAllBooksDto() {
        String sql = "SELECT b.book_id, b.title, " +
                "a.name_authors, " +
                "p.name_publishers , " +
                "c.name_categories, " +
                "b.price, b.description, b.cost_price, b.quantity, b.status, b.image_url, b.thumbnail  " +
                "FROM books b " +
                "LEFT JOIN authors a ON b.author_id = a.author_id " +
                "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
                "LEFT JOIN categories c ON b.category_id = c.category_id";

        return jdbcTemplate.query(sql, getBookMapper());
    }

    @Override
    public BooksResponse findById(Integer id) {
        String sql = "SELECT b.book_id, b.title, " +
                "a.name_authors, " +
                "p.name_publishers , " +
                "c.name_categories, " +
                "b.price, b.description, b.cost_price, b.quantity, b.status,b.image_url, b.thumbnail " +
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


    @Override
    public BooksRequest save(BooksRequest booksRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        String imageUrlJson = null;
        try {
            imageUrlJson = objectMapper.writeValueAsString(booksRequest.getImageUrl());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        booksRequest.setStatus("Actives");
        String sql = "INSERT INTO books (title, author_id, publisher_id, category_id, price, description, cost_price, quantity, status, image_url,thumbnail) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, booksRequest.getTitle(), booksRequest.getAuthorId(), booksRequest.getPublisherId(),
                booksRequest.getCategoryId(), booksRequest.getPrice(), booksRequest.getDescription(),
                booksRequest.getConsPrice(), booksRequest.getQuantity(), booksRequest.getStatus(), imageUrlJson,booksRequest.getThumbnail());
        String getLastIdSql = "SELECT LAST_INSERT_ID()";
        Integer newBookId = jdbcTemplate.queryForObject(getLastIdSql, Integer.class);
        booksRequest.setBookId(newBookId);
        return booksRequest;
    }

    @Override
    public BooksRequest update(BooksRequest booksRequest, Integer id) {
        String imageUrlStr = String.join(",", booksRequest.getImageUrl());
        String sql = "UPDATE books SET title = ?, author_id = ?, publisher_id = ?, category_id = ?, price = ?, " +
                "description = ?, cost_price = ?, quantity = ?, status = ?, image_url=?, thumbnail=?  WHERE book_id = ?";
        jdbcTemplate.update(sql, booksRequest.getTitle(), booksRequest.getAuthorId(), booksRequest.getPublisherId(),
                booksRequest.getCategoryId(), booksRequest.getPrice(), booksRequest.getDescription(),
                booksRequest.getConsPrice(), booksRequest.getQuantity(), booksRequest.getStatus(), imageUrlStr, booksRequest.getThumbnail(), id);
        booksRequest.setBookId(id);
        return booksRequest;
    }

    @Override
    public void deleteById(boolean delete, Integer id) {
        String sql = "UPDATE books SET deleted_at = ? WHERE book_id = ?";
        if (delete) {
            jdbcTemplate.update(sql, true, id);
        } else {

            jdbcTemplate.update(sql, false, id);
        }
    }

    @Override
    public PageResponse<BooksResponse> findBooksPage( int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT b.book_id, b.title, " +
                "a.name_authors, " +
                "p.name_publishers, " +
                "c.name_categories, " +
                "b.price, b.description, b.cost_price, b.quantity, b.status, b.image_url, b.thumbnail, a.author_id, p.publisher_id, c.category_id " +
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

    @Override
    public PageResponse<BooksResponse> findBooksPage2(int pageNumber, int pageSize, String filter) {
        int offset = (pageNumber - 1) * pageSize;

        String sql;
        Object[] params;

        if (filter == null || filter.isEmpty()) {
            sql = "SELECT b.book_id, b.title, " +
                    "a.name_authors, p.name_publishers, c.name_categories, " +
                    "b.price, b.description, b.cost_price, b.quantity, b.status, b.image_url, b.thumbnail, " +
                    "a.author_id, p.publisher_id, c.category_id " +
                    "FROM books b " +
                    "LEFT JOIN authors a ON b.author_id = a.author_id " +
                    "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
                    "LEFT JOIN categories c ON b.category_id = c.category_id " +
                    "LIMIT ? OFFSET ?";
            params = new Object[]{pageSize, offset};
        } else {
            sql = "SELECT b.book_id, b.title, " +
                    "a.name_authors, p.name_publishers, c.name_categories, " +
                    "b.price, b.description, b.cost_price, b.quantity, b.status, b.image_url, b.thumbnail, " +
                    "a.author_id, p.publisher_id, c.category_id " +
                    "FROM books b " +
                    "LEFT JOIN authors a ON b.author_id = a.author_id " +
                    "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
                    "LEFT JOIN categories c ON b.category_id = c.category_id " +
                    "WHERE b.title LIKE ? OR a.name_authors LIKE ? OR p.name_publishers LIKE ? OR c.name_categories LIKE ? " +
                    "LIMIT ? OFFSET ?";
            params = new Object[]{"%" + filter + "%", "%" + filter + "%", "%" + filter + "%", "%" + filter + "%", pageSize, offset};
        }
        List<BooksResponse> books = jdbcTemplate.query(sql, getBookMapper(), params);

        String countSql;
        Object[] countParams;

        if (filter == null || filter.isEmpty()) {
            countSql ="SELECT COUNT(*) FROM books b " +
                    "LEFT JOIN authors a ON b.author_id = a.author_id " +
                    "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
                    "LEFT JOIN categories c ON b.category_id = c.category_id";
            countParams = new Object[]{};
        } else {
            // Nếu có filter, đếm theo điều kiện LIKE
            countSql = "SELECT COUNT(*) FROM books b " +
                    "LEFT JOIN authors a ON b.author_id = a.author_id " +
                    "LEFT JOIN publishers p ON b.publisher_id = p.publisher_id " +
                    "LEFT JOIN categories c ON b.category_id = c.category_id " +
                    "WHERE b.title LIKE ? OR a.name_authors LIKE ? OR p.name_publishers LIKE ? OR c.name_categories LIKE ?";
            countParams = new Object[]{"%" + filter + "%", "%" + filter + "%", "%" + filter + "%", "%" + filter + "%"};
        }

        int totalElements = jdbcTemplate.queryForObject(countSql, Integer.class, countParams);


        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageResponse<>(books, totalElements, totalPages, pageNumber);
    }


}
