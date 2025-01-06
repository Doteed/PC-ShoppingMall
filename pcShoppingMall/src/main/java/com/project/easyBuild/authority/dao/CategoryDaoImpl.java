package com.project.easyBuild.authority.dao;

import com.project.easyBuild.authority.controller.ProductController;
import com.project.easyBuild.authority.dto.CategoryDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class CategoryDaoImpl implements CategoryDao {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CategoryDto> rowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
 
    @Autowired
    public CategoryDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = (rs, rowNum) -> {
            CategoryDto dto = new CategoryDto();
            dto.setCategoryId(rs.getLong("CATEGORY_ID"));
            dto.setParentId(rs.getLong("PARENT_ID"));
            dto.setCategoryName(rs.getString("CATEGORY_NAME"));
            dto.setCategoryCode(rs.getString("CATEGORY_CODE"));
            dto.setCategoryLevel(rs.getInt("CATEGORY_LEVEL"));
            dto.setSortOrder(rs.getInt("SORT_ORDER"));
            return dto;
        };
    }

    @Override
    public List<CategoryDto> listAll() {
        String sql = "SELECT * FROM CATEGORY ORDER BY CATEGORY_LEVEL, SORT_ORDER, CATEGORY_NAME";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public int insert(CategoryDto dto) {
        String sql = "INSERT INTO CATEGORY (PARENT_ID, CATEGORY_NAME, CATEGORY_CODE, CATEGORY_LEVEL, SORT_ORDER) "
                   + "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            dto.getParentId(),
            dto.getCategoryName(),
            dto.getCategoryCode(),
            dto.getCategoryLevel(),
            dto.getSortOrder()
        );
    }

    @Override
    public int update(CategoryDto dto) {
        try {
            String sql = "UPDATE CATEGORY SET PARENT_ID = ?, CATEGORY_NAME = ?, CATEGORY_CODE = ?, CATEGORY_LEVEL = ?, SORT_ORDER = ? WHERE CATEGORY_ID = ?";
            return jdbcTemplate.update(sql, 
                dto.getParentId(), 
                dto.getCategoryName(), 
                dto.getCategoryCode(), 
                dto.getCategoryLevel(), 
                dto.getSortOrder(), 
                dto.getCategoryId());
        } catch (DataAccessException e) {
        	logger.error("Error updating category: " + dto.getCategoryId(), e);
            throw new RuntimeException("Failed to update category", e);
        }
    }


    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM CATEGORY WHERE CATEGORY_ID = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    @Override
    public void insertBatch(List<CategoryDto> categories) {
        String sql = "INSERT INTO CATEGORY (CATEGORY_ID, PARENT_ID, CATEGORY_NAME, CATEGORY_CODE, CATEGORY_LEVEL, SORT_ORDER) VALUES (SEQ_CATEGORY.NEXTVAL, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CategoryDto category = categories.get(i);
                ps.setObject(1, category.getParentId());
                ps.setString(2, category.getCategoryName());
                ps.setString(3, category.getCategoryCode());
                ps.setInt(4, category.getCategoryLevel());
                ps.setInt(5, category.getSortOrder() != null ? category.getSortOrder() : 0);
            }

            @Override
            public int getBatchSize() {
                return categories.size();
            }
        });
    }
    
    @Override
    public List<CategoryDto> getCategoriesByParentAndLevel(Long parentId, int level) {
        String sql = "SELECT * FROM CATEGORY WHERE PARENT_ID = ? AND CATEGORY_LEVEL = ?";
        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new CategoryDto(
                rs.getLong("CATEGORY_ID"),
                rs.getLong("PARENT_ID"),
                rs.getString("CATEGORY_NAME"),
                rs.getString("CATEGORY_CODE"),
                rs.getInt("CATEGORY_LEVEL"),
                rs.getInt("SORT_ORDER")
            ),
            parentId, level
        );
    }

    @Override
    public void insertProductCategory(int productId, Integer categoryId) {
        String sql = "INSERT INTO PRODUCT_CATEGORY (PRODUCT_ID, CATEGORY_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, productId, categoryId);
    }
    
    @Override
    public List<CategoryDto> getCategoriesByLevel(int level, Long parentId) {
        String sql = "SELECT * FROM CATEGORY WHERE CATEGORY_LEVEL = :level";
        if (parentId != null) {
            sql += " AND PARENT_ID = :parentId";
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("level", level)
            .addValue("parentId", parentId);
        
        return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) ->
            new CategoryDto(
                rs.getLong("CATEGORY_ID"),
                rs.getLong("PARENT_ID"),
                rs.getString("CATEGORY_NAME"),
                rs.getString("CATEGORY_CODE"),
                rs.getInt("CATEGORY_LEVEL"),
                rs.getInt("SORT_ORDER")
            )
        );
    }
    
    @Override
    public boolean existsByCategoryCode(String categoryCode) {
        String sql = "SELECT COUNT(*) FROM CATEGORY WHERE CATEGORY_CODE = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, categoryCode);
        return count > 0;
    }
    
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM CATEGORY WHERE CATEGORY_ID = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count > 0;
    }


}
