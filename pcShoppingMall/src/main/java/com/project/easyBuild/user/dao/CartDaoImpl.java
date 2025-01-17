package com.project.easyBuild.user.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.user.dto.CartDto;

@Repository
public class CartDaoImpl implements CartDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final int authId = 2; // 관리자 권한

	private RowMapper<CartDto> rowMapper = (rs, rowNum) -> {
		CartDto cart = new CartDto();
		
		//cart
		cart.setCartId(rs.getInt("cart_id"));
		cart.setUserId(rs.getString("user_id"));
		cart.setProductId(rs.getInt("product_id"));
		cart.setQuantity(rs.getInt("quantity"));
		cart.setSelected(rs.getString("selected"));
		cart.setCartDate(rs.getDate("cart_date"));

		//product
		cart.setProductName(rs.getString("p_name"));
		cart.setProductPrice(rs.getInt("p_price"));
		cart.setProductImageUrl(rs.getString("image_url"));

		cart.setAvgRating(rs.getDouble("avg_rating"));
		
		return cart;
	};

	@Override
	public List<CartDto> mylistAll(String userId) {
		String sql = "SELECT c.*, p.P_NAME, p.P_PRICE, p.IMAGE_URL,"
				+ " NVL(avg_reviews.avg_rating, 0) AS avg_rating "
				+ " FROM CART c "
				+ " JOIN PRODUCT p ON c.PRODUCT_ID = p.PRODUCT_ID "
				+ " LEFT JOIN ( "
				+ " SELECT PRODUCT_ID, AVG(RATING) AS avg_rating"
				+ " FROM REVIEW WHERE IS_DELETED = 'N' "
				+ " GROUP BY PRODUCT_ID ) avg_reviews "
				+ " ON c.PRODUCT_ID = avg_reviews.PRODUCT_ID "
				+ " WHERE c.USER_ID = ? ORDER BY c.CART_ID DESC";

		return jdbcTemplate.query(sql, new Object[] { userId }, rowMapper);
	}

	@Override
	public int update(List<Integer> cartIds, List<Integer> quantities, String userId) {
		StringBuilder sql = new StringBuilder("UPDATE CART SET QUANTITY = CASE ");
	  
	    for (int i = 0; i < cartIds.size(); i++) {
	        sql.append("WHEN CART_ID = ? THEN ? ");
	    }

	    sql.append("ELSE QUANTITY END WHERE USER_ID = ?");

	    //cartIds와 quantities을 동적으로 추가
	    List<Object> params = new ArrayList<>();
	    for (int i = 0; i < cartIds.size(); i++) {
	        params.add(cartIds.get(i));
	        params.add(quantities.get(i));
	    }
	    params.add(userId);
	    
	    return jdbcTemplate.update(sql.toString(), params.toArray());
	}
	
	@Override
	public int update(String userId, List<Integer> cartIds, List<String> selecteds) {
		StringBuilder sql = new StringBuilder("UPDATE CART SET SELECTED = CASE ");
	  
		//cartIds와 selecteds을 동적으로 추가
	    for(int i = 0; i < cartIds.size(); i++) {
	        sql.append("WHEN CART_ID = ? THEN ? ");
	    }

	    sql.append("ELSE SELECTED END WHERE USER_ID = ?");

	    List<Object> params = new ArrayList<>();
	    for(int i = 0; i < cartIds.size(); i++) {
	        params.add(cartIds.get(i));
	        params.add(selecteds.get(i));
	    }
	    params.add(userId);
	    
	    return jdbcTemplate.update(sql.toString(), params.toArray());
	}

	@Override
    public int insert(int productId, int quantity, String userId) {
        String sql = " INSERT INTO CART "
                   + " VALUES (SEQ_CART.NEXTVAL, ?, ?, ?, SYSDATE, 'N')";

        return jdbcTemplate.update(sql, userId, productId, quantity);
    }
	
	@Override
	public int delete(List<Integer> cartIds, String userId) {
		StringBuilder sql = new StringBuilder("DELETE FROM CART WHERE CART_ID IN (");
		
		//동적으로 ?를 추가
	    for(int i = 0; i < cartIds.size(); i++) {
	        sql.append("?");
	        if (i < cartIds.size()-1) {
	            sql.append(", ");
	        }
	    }
	    
	    sql.append(") AND USER_ID = ?");
	    
	    List<Object> params = new ArrayList<>();
	    for(int i = 0; i < cartIds.size(); i++) {
	        params.add(cartIds.get(i));
	    }
	    params.add(userId);
	    
		return jdbcTemplate.update(sql.toString(), params.toArray());
	}
	
    @Override
    public int deleteAll(String userId) {
        String sql = " DELETE FROM CART WHERE USER_ID = ? ";

        return jdbcTemplate.update(sql, userId);
    }

	@Override
	public boolean isCartNotEmpty(String userId) {
		String sql = "SELECT CASE WHEN EXISTS (SELECT 1 FROM CART WHERE USER_ID = ?) THEN 1 ELSE 0 END AS result FROM dual";
		return jdbcTemplate.queryForObject(sql, Boolean.class, userId);
	}
}
