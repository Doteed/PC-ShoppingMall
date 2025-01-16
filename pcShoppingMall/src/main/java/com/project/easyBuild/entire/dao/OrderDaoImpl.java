package com.project.easyBuild.entire.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.authority.controller.ProductController;
import com.project.easyBuild.authority.dto.CategoryDto;
import com.project.easyBuild.entire.dto.OrderDto;
import com.project.easyBuild.user.dto.OrderRequestDto;


@Repository
public class OrderDaoImpl implements OrderDao {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// private static final int authId = 2; // 관리자 권한

	private RowMapper<OrderDto> orderRowMapper = (rs, rowNum) -> {
		OrderDto order = new OrderDto();

		// order
		order.setOrderId(rs.getInt("ORDER_ID"));
		order.setDeliveryId(rs.getInt("DELIVERY_ID"));
		order.setUserId(rs.getString("USER_ID"));
		order.setProductId(rs.getInt("AUTH_ID"));
		order.setProductId(rs.getInt("PRODUCT_ID"));
		order.setTotalPrice(rs.getInt("TOTAL_PRICE"));
		order.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
		order.setOrderDate(rs.getDate("ORDER_DATE"));

		// product
		order.setProductName(rs.getString("P_NAME"));

		// delivery
		order.setDeliveryStatus(rs.getString("DELIVERY_STATUS"));
		order.setAddressee(rs.getString("ADDRESSEE"));
		order.setAddress(rs.getString("ADDRESS"));
		order.setPhone(rs.getString("PHONE"));
		return order;
	};

	@Override
	public List<OrderDto> mylistAll(String userId) {
		String sql = "SELECT ot.*, p.P_NAME, d.DELIVERY_STATUS, d.ADDRESSEE, d.ADDRESS, d.PHONE "
				+ " FROM ORDER_TABLE ot " + " JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID "
				+ " JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID " + " WHERE ot.USER_ID = ? "
				+ " ORDER BY ot.ORDER_ID DESC";
		return jdbcTemplate.query(sql, new Object[] { userId }, orderRowMapper);
	}

	@Override
	public List<OrderDto> listAll() {
		String sql = "SELECT ot.*, p.P_NAME, d.DELIVERY_STATUS, d.ADDRESSEE, d.ADDRESS, d.PHONE"
				+ " FROM ORDER_TABLE ot " + " JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID "
				+ " JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID " + " ORDER BY ot.ORDER_ID DESC";
		return jdbcTemplate.query(sql, orderRowMapper);
	}

	@Override
	public OrderDto listOne(int orderId, String userId) {
		String sql = "SELECT ot.*, p.P_NAME, d.DELIVERY_STATUS, d.ADDRESSEE, d.ADDRESS, d.PHONE"
				+ " FROM ORDER_TABLE ot " + " JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID "
				+ " JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID " + " WHERE ot.ORDER_ID = ? AND ot.USER_ID = ?";

		try {
			return jdbcTemplate.queryForObject(sql, orderRowMapper, orderId, userId);
		} catch (EmptyResultDataAccessException e) {
			return null; // 데이터 못 찾았을 때
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("주문/배송 조회 중 오류가 발생했습니다.");
		}
	}
	
	// 배송 정보 업데이트(사용자)
	@Override
	public int update(OrderDto dto, String userId) {
		String sql = "UPDATE DELIVERY d SET d.ADDRESSEE = ?, d.ADDRESS = ?, d.PHONE = ? "
				+ " WHERE d.DELIVERY_ID = ? AND d.DELIVERY_ID IN ( "
				+ " SELECT DELIVERY_ID FROM ORDER_TABLE ot WHERE ot.USER_ID = ?)";

		System.out.println(dto);
		return jdbcTemplate.update(sql, dto.getAddressee(), dto.getAddress(), dto.getPhone(), dto.getDeliveryId(),
				userId);
	}

	// TODO: 컨트롤러에서 관리자인지 확인 필요.
	// 배송 상태 업데이트(관리자)
	@Override
	public int update(int deliveryId, String deliveryStatus) {
		String sql = "UPDATE DELIVERY SET DELIVERY_STATUS = ? WHERE DELIVERY_ID = ?";

		return jdbcTemplate.update(sql, deliveryStatus, deliveryId);
	}

	@Override
	public int cancle(int orderId, String userId, int authId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE DELIVERY d SET d.DELIVERY_STATUS = ? ").append("WHERE d.DELIVERY_ID = ( ")
				.append("SELECT ot.DELIVERY_ID FROM ORDER_TABLE ot ").append("WHERE ot.ORDER_ID = ? ");

		List<Object> params = new ArrayList<>();
		params.add("취소");
		params.add(orderId);

		if (authId == 2) {
			sql.append(")"); // 관리자일때 조건없이 닫기만
		} else if (authId == 1) {
			sql.append(" AND ot.USER_ID = ? )"); // 일반 유저는 본인글만
			params.add(userId);
		}

		return jdbcTemplate.update(sql.toString(), params.toArray());
	}

	@Override
	public Map<String, Integer> count(String userId) {
		String sql = "SELECT DELIVERY_STATUS, COUNT(*) FROM ORDER_TABLE ot "
				+ "JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID "
				+ "WHERE ot.USER_ID = ? GROUP BY DELIVERY_STATUS";

		// 초기화
		List<String> predefinedStatuses = List.of("입금대기", "결제완료", "배송중", "배송완료", "취소");
		Map<String, Integer> result = predefinedStatuses.stream()
				.collect(Collectors.toMap(status -> status, status -> 0));

		jdbcTemplate.query(sql, new Object[] { userId }, rs -> {
			while (rs.next()) {
				String status = rs.getString("DELIVERY_STATUS");
				int count = rs.getInt("COUNT(*)");
				result.put(status, count);
			}
			return null; // 업데이트만
		});

		return result;
	}

	@Override
	public int insertFromCart(OrderRequestDto dto) {
		System.out.println(dto.getCartIds());
	    //delivery insert
	    String deliverySql = " INSERT INTO DELIVERY " +
	                         " VALUES (SEQ_DELIVERY.NEXTVAL, ?, ?, ?, ?)";

	    //keyholder를 사용해 insert한 delivery_id를 반환받음
	    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	    jdbcTemplate.update(connection -> {
	        PreparedStatement ps = connection.prepareStatement(deliverySql, new String[]{"DELIVERY_ID"});
	        ps.setString(1, dto.getAddressee());
	        ps.setString(2, dto.getAddress());
	        ps.setString(3, dto.getPhone());
	        ps.setString(4, dto.getPaymentMethod().equals("카드")? "결제완료" : "입금대기");
	        return ps;
	    }, keyHolder);

	    Number deliveryId = keyHolder.getKey();
	    System.out.println("delivery id : " + deliveryId);
	    if (deliveryId == null) {
	        throw new RuntimeException("배송 정보를 삽입을 실패했습니다.");
	    }
	    
	    //order_table insert
	    String orderSql = "INSERT INTO ORDER_TABLE " +
	                      " (ORDER_ID, DELIVERY_ID, USER_ID, AUTH_ID, PRODUCT_ID, TOTAL_PRICE, PAYMENT_METHOD, ORDER_DATE) " +
	                      " SELECT SEQ_ORDER_TABLE.NEXTVAL, :deliveryId, :userId, :authId, c.PRODUCT_ID, (c.QUANTITY * p.P_PRICE), :paymentMethod, SYSDATE " +
	                      " FROM CART c " +
	                      " JOIN PRODUCT p ON c.PRODUCT_ID = p.PRODUCT_ID " +
	                      " WHERE c.CART_ID IN (:cartIds)";

	    System.out.println("order sql : " + orderSql);
	    
	    //cartIds 바인딩
	    MapSqlParameterSource parameters = new MapSqlParameterSource();
	    parameters.addValue("deliveryId", deliveryId.intValue());
	    parameters.addValue("userId", dto.getUserId());
	    parameters.addValue("authId", dto.getAuthId());
	    parameters.addValue("paymentMethod", dto.getPaymentMethod());
	    parameters.addValue("cartIds", dto.getCartIds());

	    NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	    int result = 0;
	    try {
	        result = namedJdbcTemplate.update(orderSql, parameters);
	        System.out.println("SQL executed successfully, result: " + result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("SQL execution failed: " + e.getMessage());
	    }

//	    if (result > 0) {
//	    	//결제된 cart 데이터 삭제
////	        String deleteCartSql = "DELETE FROM CART WHERE CART_ID IN (:cartIds)";
////	        namedJdbcTemplate.update(deleteCartSql, parameters);
//	    }
	    
	    return result;
	}
	
	//관리자 주문 수정
	@Override
	public int updateOrder(OrderDto dto, String userId) {
	    logger.info("updateOrder 메서드 시작: OrderId={}, UserId={}", dto.getOrderId(), userId);
	    
	    // ORDER_TABLE 업데이트
	    String sql1 = "UPDATE ORDER_TABLE ot SET ot.TOTAL_PRICE = ?, ot.PAYMENT_METHOD = ? WHERE ot.ORDER_ID = ?";
	    int result1 = jdbcTemplate.update(sql1, dto.getTotalPrice(), dto.getPaymentMethod(), dto.getOrderId());

	    // DELIVERY 테이블 업데이트
	    String sql2 = "UPDATE DELIVERY d SET d.ADDRESSEE = ?, d.ADDRESS = ?, d.PHONE = ?, d.DELIVERY_STATUS = ? WHERE d.DELIVERY_ID = (SELECT DELIVERY_ID FROM ORDER_TABLE WHERE ORDER_ID = ?)";
	    int result2 = jdbcTemplate.update(sql2, dto.getAddressee(), dto.getAddress(), dto.getPhone(), dto.getDeliveryStatus(), dto.getOrderId());
  
	    int finalResult = (result1 > 0 && result2 > 0) ? 1 : 0;
	    logger.info("updateOrder 메서드 종료: 최종 결과={}", finalResult);
	    return finalResult;
	}
	
	//월별 매출
	public List<OrderDto> getMonthlySales(int year) {
	    String sql ="SELECT EXTRACT(MONTH FROM ot.order_date) as month, "
	    			+ "SUM(CASE WHEN d.delivery_status != '취소' THEN ot.total_price ELSE 0 END) as total_sales, " 
	    			+ "SUM(CASE WHEN d.delivery_status = '취소' THEN ot.total_price ELSE 0 END) as cancelled_sales " 
	    			+ "FROM order_table ot " 
	    			+ "JOIN delivery d ON ot.delivery_id = d.delivery_id " 
	    			+ "WHERE EXTRACT(YEAR FROM ot.order_date) = ? " 
	    			+ "GROUP BY EXTRACT(MONTH FROM ot.order_date) ORDER BY month";

	    return jdbcTemplate.query(sql, new Object[]{year}, (rs, rowNum) -> {
	        OrderDto dto = new OrderDto();
	        dto.setMonth(rs.getInt("month"));
	        dto.setTotalPrice(rs.getInt("total_sales"));
	        dto.setCancelledSales(rs.getInt("cancelled_sales"));
	        return dto;
	    });
	}

	//관리자 리스트 디테일
	@Override
	public OrderDto authListOne(int orderId) {
	    String sql = "SELECT ot.*, p.P_NAME, d.DELIVERY_STATUS, d.ADDRESSEE, d.ADDRESS, d.PHONE"
	        + " FROM ORDER_TABLE ot "
	        + " JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID "
	        + " JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID "
	        + " WHERE ot.ORDER_ID = ?";
	    try {
	        return jdbcTemplate.queryForObject(sql, orderRowMapper, orderId);
	    } catch (EmptyResultDataAccessException e) {
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("주문/배송 조회 중 오류가 발생했습니다.");
	    }
	}

}
