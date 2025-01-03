package com.project.easyBuild.authority.dao;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.authority.dto.ProductDto;

@Repository
public class ProductDaoImple implements ProductDao {
	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<ProductDto> rowMapper;

    @Autowired
    public ProductDaoImple(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (rs, rowNum) -> {
            ProductDto dto = new ProductDto();
            dto.setProductId(rs.getInt("PRODUCT_ID"));
            dto.setUserId(rs.getString("USER_ID"));
            dto.setCategoryId(rs.getInt("CATEGORY_ID"));
            dto.setAuthId(rs.getInt("AUTH_ID"));
            dto.setImageUrl(rs.getString("IMAGE_URL"));
            dto.setpName(rs.getString("P_NAME"));
            dto.setpStock(rs.getInt("P_STOCK"));
            dto.setOrderWaiting(rs.getInt("ORDER_WAITING"));
            dto.setAvailableStock(rs.getInt("AVAILABLE_STOCK"));
            dto.setpReportstock(rs.getInt("P_REPORTSTOCK"));
            dto.setpSale(rs.getString("P_SALE"));
            dto.setpSoldout(rs.getString("P_SOLDOUT"));
            dto.setpPrice(rs.getInt("P_PRICE"));
            dto.setpEnroll(rs.getDate("P_ENROLL"));
            return dto;
        };
    }

    @Override
    public List<ProductDto> listAll() {
        String sql = "SELECT * FROM PRODUCT";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public int updateStockStatus(int productId, int stock) {
        String sql = "UPDATE PRODUCT SET P_STOCK = ? WHERE PRODUCT_ID = ?";
        return jdbcTemplate.update(sql, stock, productId);
    }

    @Override
    public int insert(ProductDto dto) {
        String sql = "INSERT INTO PRODUCT (PRODUCT_ID, USER_ID, CATEGORY_ID, AUTH_ID, P_PRICE, P_NAME, P_STOCK, P_ENROLL, IMAGE_URL) " +
                     "VALUES (SEQ_PRODUCT.NEXTVAL, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?)";
        return jdbcTemplate.update(sql, dto.getUserId(), dto.getCategoryId(), dto.getAuthId(), 
                                   dto.getpPrice(), dto.getpName(), dto.getpStock(), dto.getImageUrl());
    }
    
    @Override
    public int updateProductImage(Integer productId, String imageUrl) {
        return jdbcTemplate.update(
            "UPDATE PRODUCT SET IMAGE_URL = ? WHERE PRODUCT_ID = ?",
            imageUrl, productId
        );
    }

    @Override
    public String getProductImageUrl(Integer productId) {
        try {
        	return jdbcTemplate.queryForObject("SELECT IMAGE_URL FROM PRODUCT WHERE PRODUCT_ID = ?", String.class, productId);
        } catch (EmptyResultDataAccessException e) {
            return null; // 제품이 없을 경우 null 반환
        }
    }
    
    @Override
    public Page<ProductDto> listAllPaginated(Pageable pageable) {
        String query = """
            SELECT * FROM (
                SELECT A.*, ROWNUM AS RN 
                FROM (
                    SELECT * FROM PRODUCT ORDER BY P_ENROLL DESC
                ) A 
                WHERE ROWNUM <= ?
            ) WHERE RN > ?
        """;
        int startItem = pageable.getPageNumber() * pageable.getPageSize();
        int pageSize = pageable.getPageSize();

        List<ProductDto> products = jdbcTemplate.query(query, rowMapper, startItem + pageSize, startItem);
        long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM PRODUCT", Long.class);
        return new PageImpl<>(products, pageable, total);
    }
    
    @Override
    public int updateStock(int productId, int quantity) {
        String sql = "UPDATE PRODUCT SET P_STOCK = P_STOCK + ? WHERE PRODUCT_ID = ?";
        return jdbcTemplate.update(sql, quantity, productId);
    }

    @Override
    public int updateSaleStatus(int productId, String status) {
        String sql = "UPDATE PRODUCT SET P_SALE = ? WHERE PRODUCT_ID = ?";
        return jdbcTemplate.update(sql, status, productId);
    }

    @Override
    public int updateSoldOutStatus(int productId, String pSoldout) {
        String sql = "UPDATE PRODUCT SET P_SOLDOUT = ? WHERE PRODUCT_ID = ?";
        return jdbcTemplate.update(sql, pSoldout, productId);
    }
    
    @Override
    public int updateProduct(int productId, int stock, int pReportstock, String saleStatus, String pSoldout) {
        String sql = "UPDATE PRODUCT SET P_STOCK = ?, P_REPORTSTOCK = ?, P_SALE = ?, P_SOLDOUT = ? WHERE PRODUCT_ID = ?";
        return jdbcTemplate.update(sql, stock, pReportstock, saleStatus, pSoldout, productId);
    }

    @Override
    public ProductDto getProductById(int productId) {
        String sql = """
            SELECT PRODUCT_ID, USER_ID, CATEGORY_ID, AUTH_ID, P_PRICE, P_NAME, P_STOCK, ORDER_WAITING,
                   AVAILABLE_STOCK, NOTIFY_QUANTITY, P_SALE, P_SOLDOUT, P_ENROLL, IMAGE_URL
            FROM PRODUCT
            WHERE PRODUCT_ID = ?
        """;

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, productId);
        } catch (EmptyResultDataAccessException e) {
            return null; // 제품이 없는 경우 null 반환
        }
    }
}
