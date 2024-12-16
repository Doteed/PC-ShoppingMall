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
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<ProductDto> rowMapper = (rs, rwoNum)->{
		ProductDto dto = new ProductDto();
		
		dto.setProductId(rs.getInt(1));
		dto.setUserId(rs.getString(2));
		dto.setCategoryId(rs.getInt(3));
		dto.setAuthId(rs.getInt(4));
		dto.setpPrice(rs.getInt(5));
		dto.setpName(rs.getString(6));
		dto.setpStock(rs.getInt(7));
		dto.setpEnroll(rs.getDate(8));
		
		return dto;
	};
	
	@Override
	public List<ProductDto> listAll(){
		List<ProductDto> list = new ArrayList<ProductDto>();
		String sql = " SELECT * FROM PRODUCT ";
		list = jdbcTemplate.query(sql, rowMapper);
		return list;
	}
	    
    @Override
    public int updateStockStatus(int productId, int stock) {
        String sql = "UPDATE PRODUCT SET P_STOCK = ? WHERE PRODUCT_ID = ?";
        return jdbcTemplate.update(sql, stock, productId);
    }

    @Override
    public void decreaseStock(int productId, int quantity) {
        String sql = "UPDATE PRODUCT " +
                     "SET P_STOCK = P_STOCK - ?, " +
                     "    P_STOCK_STATUS = CASE " +
                     "        WHEN P_STOCK - ? <= 0 THEN 'OUT_OF_STOCK' " +
                     "        ELSE P_STOCK_STATUS " +
                     "    END " +
                     "WHERE PRODUCT_ID = ? AND P_STOCK >= ?";
        
        jdbcTemplate.update(sql, quantity, quantity, productId, quantity);
    }

}
