package com.project.easyBuild.authority.dao;

import com.project.easyBuild.authority.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class CategoryDaoImpl implements CategoryDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<CategoryDto> rowMapper;

    @Autowired
    public CategoryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        String sql = "UPDATE CATEGORY SET PARENT_ID = ?, CATEGORY_NAME = ?, CATEGORY_CODE = ?, CATEGORY_LEVEL = ?, SORT_ORDER = ? WHERE CATEGORY_ID = ?";
        return jdbcTemplate.update(sql, dto.getParentId(), dto.getCategoryName(), dto.getCategoryCode(), dto.getCategoryLevel(), dto.getSortOrder(), dto.getCategoryId());
    }

    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM CATEGORY WHERE CATEGORY_ID = ?";
        return jdbcTemplate.update(sql, id);
    }
    
    @Override
    public void insertBatch(List<CategoryDto> categories) {
        String sql = "INSERT INTO CATEGORY (CATEGORY_ID, PARENT_ID, CATEGORY_NAME, CATEGORY_CODE, CATEGORY_LEVEL, SORT_ORDER) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CategoryDto category = categories.get(i);
                ps.setLong(1, category.getCategoryId());
                if (category.getParentId() != null) {
                    ps.setLong(2, category.getParentId());
                } else {
                    ps.setNull(2, Types.BIGINT);
                }
                ps.setString(3, category.getCategoryName());
                ps.setString(4, category.getCategoryCode());
                ps.setInt(5, category.getCategoryLevel());
                ps.setInt(6, category.getSortOrder());
            }

            @Override
            public int getBatchSize() {
                return categories.size();
            }
        });
    }

}
