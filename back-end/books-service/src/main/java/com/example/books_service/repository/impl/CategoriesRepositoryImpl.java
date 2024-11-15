package com.example.books_service.repository.impl;

import com.example.books_service.dto.request.CategoriesRequest;
import com.example.books_service.dto.response.CategoriesResponse;
import com.example.books_service.dto.response.PageResponse;
import com.example.books_service.exception.CustomException;
import com.example.books_service.mapper.CategoriesRowMapper;
import com.example.books_service.repository.CategoriesServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CategoriesRepositoryImpl implements CategoriesServiceRepository {


    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired
    public CategoriesRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public CategoriesRowMapper getCategoriesMapper(){
        return new CategoriesRowMapper();
    }


    @Override
    public PageResponse<CategoriesResponse> findAll(String name, int pageNum, int pageSize) {

        int offset = (pageNum - 1) * pageSize;

        // SQL với điều kiện LIKE khi name không phải null
        String sql = "SELECT c.category_id, c.name_categories, c.deleted_at " +
                "FROM categories c " +
                "WHERE (c.name_categories LIKE :name OR :name IS NULL) " +
                "AND c.deleted_at = false " +
                "LIMIT :pageSize OFFSET :offset";

        Map<String, Object> params = new HashMap<>();
        params.put("name", name != null ? "%" + name + "%" : null); // Kiểm tra name nếu null
        params.put("pageSize", pageSize);
        params.put("offset", offset);

        // Truy vấn danh sách categories
        List<CategoriesResponse> categories = namedParameterJdbcTemplate.query(
                sql, params, getCategoriesMapper()
        );

        // SQL để đếm tổng số danh mục không bị xóa
        String countSql = "SELECT COUNT(*) FROM categories c " +
                "WHERE (c.name_categories LIKE :name OR :name IS NULL) " +
                "AND c.deleted_at = false";

        int totalElements = namedParameterJdbcTemplate.queryForObject(countSql, params, Integer.class);

        // Tính số trang
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        // Trả về đối tượng PageResponse
        return new PageResponse<>(categories, totalElements, totalPages, pageNum);
    }


    @Override
    public CategoriesRequest save(CategoriesRequest categoriesRequest) {
        String sql = "INSERT INTO categories (name_categories) VALUES (:nameCategories)";
        Map<String, Object> params = new HashMap<>();
        params.put("nameCategories", categoriesRequest.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
        categoriesRequest.setCategoryId(keyHolder.getKey().intValue());
        return categoriesRequest;
    }

    @Override
    public CategoriesRequest update(Integer id, CategoriesRequest categoriesRequest) {
        String sql = "UPDATE categories SET name_categories = :nameCategories WHERE category_id = :categoryId";

        Map<String, Object> params = new HashMap<>();
        params.put("nameCategories", categoriesRequest.getName());
        params.put("categoryId", id);

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);
        if (rowsAffected > 0) {
            categoriesRequest.setCategoryId(id);
            return categoriesRequest;
        } else {
            throw new CustomException("Cập nhật danh mục không thành công");
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM categories WHERE category_id = :categoryId";

        Map<String, Object> params = new HashMap<>();
        params.put("categoryId", id);

        int rowsAffected = namedParameterJdbcTemplate.update(sql, params);
        if (rowsAffected == 0) {
            throw new CustomException("Xóa danh mục không thành công");
        }
    }

}
