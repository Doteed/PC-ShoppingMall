package com.project.easyBuild.entire.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.authority.dto.CategoryDto;
import com.project.easyBuild.entire.dto.OrderDto;

@Repository
public class OrderDaoImpl implements OrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final int authId = 2; // 관리자 권한

	private RowMapper<OrderDto> orderRowMapper = (rs, rowNum) -> {
		OrderDto order = new OrderDto();

		// order
		order.setOrderId(rs.getInt("ORDER_ID"));
		order.setDeliveryId(rs.getInt("DELIVERY_ID"));
		order.setUserId(rs.getString("USER_ID"));
		order.setProductId(rs.getInt("PRODUCT_ID"));
		order.setTotalPrice(rs.getInt("TOTAL_PRICE"));
		order.setCartstatus(rs.getString("CARTSTATUS"));
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
                + " FROM ORDER_TABLE ot "
                + " JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID "
                + " JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID "
                + " WHERE ot.USER_ID = ? "
                + " ORDER BY ot.ORDER_ID DESC";
		return jdbcTemplate.query(sql, new Object[] { userId }, orderRowMapper);
	}

	@Override
	public List<OrderDto> listAll() {
		String sql = "SELECT ot.*, p.P_NAME, d.DELIVERY_STATUS, d.ADDRESSEE, d.ADDRESS, d.PHONE"
                + " FROM ORDER_TABLE ot "
                + " JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID "
                + " JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID "
                + " ORDER BY ot.ORDER_ID DESC";
		return jdbcTemplate.query(sql, orderRowMapper);
	}

	@Override
	public OrderDto listOne(int orderId, String userId) {
		String sql = "SELECT ot.*, p.P_NAME, d.DELIVERY_STATUS, d.ADDRESSEE, d.ADDRESS, d.PHONE"
                + " FROM ORDER_TABLE ot "
                + " JOIN PRODUCT p ON ot.PRODUCT_ID = p.PRODUCT_ID "
                + " JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID "
                + " WHERE ot.ORDER_ID = ? AND ot.USER_ID = ?";

		try {
			return jdbcTemplate.queryForObject(sql, orderRowMapper, orderId, userId);
		} catch (EmptyResultDataAccessException e) {
			return null; // 데이터 못 찾았을 때
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("주문/배송 조회 중 오류가 발생했습니다.");
		}
	}

    //TODO: 컨트롤러에서 세션 확인 후 없으면 리다이렉트 구현. 완료시 서브쿼리는 지울 것.
	@Override
	public int myUpdate(OrderDto dto) {
		String sql = "UPDATE DELIVERY d SET d.ADDRESSEE = ?, d.ADDRESS = ?, d.PHONE = ? "
				+ " WHERE d.DELIVERY_ID = ? AND d.DELIVERY_ID IN ( "
				+ " SELECT DELIVERY_ID FROM ORDER_TABLE ot WHERE ot.USER_ID = ?)";

		return jdbcTemplate.update(sql, dto.getAddressee(), dto.getAddress(), dto.getPhone(), dto.getDeliveryId(),
				dto.getUserId());
	}

    //TODO: 컨트롤러에서 관리자인지 확인 필요. 배송 상태 업데이트.
    @Override
    public int update(int deliveryId, String deliveryStatus) {
        String sql = "UPDATE DELIVERY d SET d.DELIVERY_STATUS = ? WHERE d.DELIVERY_ID = ?";
        
        return jdbcTemplate.update(sql, deliveryStatus, deliveryId);
    }
    
    //TODO:
	@Override
	public int cancle(int orderId, String userId) {
		String sql = "UPDATE DELIVERY d SET d.DELIVERY_STATUS = ? "
				+ "WHERE d.DELIVERY_ID = ( "
				+ "SELECT ot.DELIVERY_ID FROM ORDER_TABLE ot "
				+ "JOIN MEMBER m ON ot.USER_ID = m.USER_ID "
				+ "WHERE ot.ORDER_ID = ? "
				+ "AND (ot.USER_ID = ? OR m.AUTH_ID = ?) " + ")";

		return jdbcTemplate.update(sql, "취소", orderId, userId, authId);
	}

	@Override
	public Map<String, Integer> count(String userId) {
		String sql = "SELECT DELIVERY_STATUS, COUNT(*) FROM ORDER_TABLE ot "
				+ "JOIN DELIVERY d ON ot.DELIVERY_ID = d.DELIVERY_ID "
				+ "WHERE ot.USER_ID = ? GROUP BY DELIVERY_STATUS";

        //초기화
        List<String> predefinedStatuses = List.of("입금대기", "결제완료", "배송중", "배송완료", "취소");
        Map<String, Integer> result = predefinedStatuses.stream()
                .collect(Collectors.toMap(status -> status, status -> 0));
        
        jdbcTemplate.query(sql, new Object[] { userId }, rs -> {
            while (rs.next()) {
                String status = rs.getString("DELIVERY_STATUS");
                int count = rs.getInt("COUNT(*)");
                result.put(status, count);
            }
            return null; //업데이트만
        });

        return result;
	}
	
	@Override
	public int insert (OrderDto dto) {
		String sql = " INSERT INTO ORDER_TABLE ot VALUES (orderId, deliveryId, userId, productId, totalPrice, cartstatus, paymentMethod, orderDate, productName, deliveryStatus, addressee, address, phone) ";
		return jdbcTemplate.update(sql, 
				dto.getOrderId(),
				dto.getDeliveryId(),
				dto.getUserId(),
				dto.getProductId(),
				dto.getTotalPrice(),
				dto.getCartstatus(),
				dto.getPaymentMethod(),
				dto.getOrderDate(),
				dto.getProductName(),
				dto.getDeliveryStatus(),
				dto.getAddressee(),
				dto.getAddress(),
				dto.getPhone()				
				);
	}

}
