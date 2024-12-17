package com.project.easyBuild.product.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.product.dto.ProductDto;

@Repository
public class ProductDaoImple implements ProductDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ProductDto> rowMapper = (rs, rowNum) -> {
        ProductDto dto = new ProductDto();
        dto.setProductId(rs.getInt("PRODUCT_ID"));
        dto.setUserId(rs.getString("USER_ID"));
        dto.setCategoryId(rs.getInt("CATEGORY_ID"));
        dto.setAuthId(rs.getInt("AUTH_ID"));
        dto.setpPrice(rs.getInt("P_PRICE"));
        dto.setpName(rs.getString("P_NAME"));
        dto.setpStock(rs.getInt("P_STOCK"));
        dto.setpEnroll(rs.getDate("P_ENROLL"));
        return dto;
    };

    @Autowired
    public ProductDaoImple(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ProductDto> listAll() {
        return jdbcTemplate.query("SELECT * FROM PRODUCT", rowMapper);
    }

    @Override
    public int updateStockStatus(int productId, int stock) {
        return jdbcTemplate.update("UPDATE PRODUCT SET P_STOCK = ? WHERE PRODUCT_ID = ?", stock, productId);
    }

    @Override
    public void decreaseStock(int productId, int quantity) {
        jdbcTemplate.update(
            "UPDATE PRODUCT SET P_STOCK = P_STOCK - ?, P_STOCK_STATUS = CASE WHEN P_STOCK - ? <= 0 THEN 'OUT_OF_STOCK' ELSE P_STOCK_STATUS END WHERE PRODUCT_ID = ? AND P_STOCK >= ?",
            quantity, quantity, productId, quantity
        );
    }

    @Override
    public int insert(ProductDto dto) {
        return jdbcTemplate.update(
            "INSERT INTO PRODUCT (PRODUCT_ID, USER_ID, CATEGORY_ID, AUTH_ID, P_PRICE, P_NAME, P_STOCK, P_ENROLL) VALUES (SEQ_PRODUCT.NEXTVAL, ?, ?, ?, ?, ?, ?, CURRENT_DATE)",
            dto.getUserId(), dto.getCategoryId(), dto.getAuthId(), dto.getpPrice(), dto.getpName(), dto.getpStock()
        );
    }
}
