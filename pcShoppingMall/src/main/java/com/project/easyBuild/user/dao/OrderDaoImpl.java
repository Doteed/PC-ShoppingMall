package com.project.easyBuild.user.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.user.dto.OrderDto;

@Repository
public class OrderDaoImpl implements OrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final int authId = 1; //관리자 권한 //삭제 의논

	private RowMapper<OrderDto> rowMapper = (rs, rowNum) -> {
		OrderDto order = new OrderDto();
	    
		//order
	    order.setOrderId(rs.getInt("order_id"));
	    order.setDeliveryId(rs.getInt("delivery_id"));
	    order.setUserId(rs.getString("user_id"));
	    order.setProductId(rs.getInt("product_id"));
	    order.setTotalPrice(rs.getInt("total_price"));
	    order.setCartStatus(rs.getBoolean("cartStatus"));
	    order.setOrderDate(rs.getDate("order_date"));
	    
	    //상품
	    order.setProductName(rs.getString("P_NAME"));

	    return order;
	};
	
	@Override
	public List<OrderDto> mylistAll(String userId){
		String sql = "SELECT ot.*, p.P_NAME " +
				" FROM ORDER_TABLE ot " +
				" JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID " +
				" JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID " +
				" WHERE ot.USER_ID = ? ";
		return jdbcTemplate.query(sql, new Object[]{userId}, rowMapper);
	}

	@Override
	public List<OrderDto> listAll() {
		String sql = "SELECT ot.*, p.P_NAME " +
				" FROM ORDER_TABLE ot " +
				" JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID " +
				" JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID ";
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public OrderDto listOne(int orderId, String userId) {
	    String sql = "SELECT * FROM ORDER_TABLE WHERE ORDER_ID = ? AND USER_ID = ?";
	    try {
	        return jdbcTemplate.queryForObject(sql, new Object[]{orderId, userId}, rowMapper);
	    } catch (EmptyResultDataAccessException e) {
	        return null; //데이터 못 찾았을 때
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("주문/배송 조회 중 오류가 발생했습니다.");
	    }
	}

	@Override //삭제 의논
	public int update(OrderDto dto) {
//	    String sql = "UPDATE QA SET TITLE = ?, CONTENT = ? WHERE QA_ID = ? AND (USER_ID = ? OR AUTH_ID = ?)";
//	    
//	    return jdbcTemplate.update(sql, dto.getTitle(), dto.getContent(), dto.getQaId(), dto.getUserId(), authId);
		return 0;
	}

	@Override
	public int delete(int orderId, String userId) {
	    String sql = "DELETE FROM ORDER_TABLE WHERE ORDER_ID = ? AND (USER_ID = ? OR AUTH_ID = ?)";
	    
	    return jdbcTemplate.update(sql, orderId, userId, authId);
	}
}
