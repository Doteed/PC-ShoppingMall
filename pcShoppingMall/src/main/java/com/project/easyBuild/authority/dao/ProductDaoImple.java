package com.project.easyBuild.authority.dao;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
        dto.setImageUrl(rs.getString("IMAGE_URL"));
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
            "INSERT INTO PRODUCT (PRODUCT_ID, USER_ID, CATEGORY_ID, AUTH_ID, P_PRICE, P_NAME, P_STOCK, P_ENROLL, IMAGE_URL) VALUES (SEQ_PRODUCT.NEXTVAL, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?)",
            dto.getUserId(), dto.getCategoryId(), dto.getAuthId(), dto.getpPrice(), dto.getpName(), dto.getpStock(), dto.getImageUrl()
        );
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
    	String result = jdbcTemplate.queryForObject("SELECT column FROM table WHERE condition = ?", String.class, rowMapper);
		return result;
    }
    
    @Override
    public Page<ProductDto> listAllPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        String countQuery = "SELECT COUNT(*) FROM PRODUCT";
        String query = "SELECT * FROM ("
        		+ "    SELECT A.*, ROWNUM RN FROM ("
        		+ "        SELECT * FROM PRODUCT ORDER BY P_ENROLL DESC"
        		+ "    ) A WHERE ROWNUM <= :limit"
        		+ ") WHERE RN > :offset"
        		+ "";
        
        long total = jdbcTemplate.queryForObject(countQuery, Long.class);
        
        List<ProductDto> products = jdbcTemplate.query(query, rowMapper, startItem + pageSize, startItem);
        
        return new PageImpl<>(products, pageable, total);
    }
}
