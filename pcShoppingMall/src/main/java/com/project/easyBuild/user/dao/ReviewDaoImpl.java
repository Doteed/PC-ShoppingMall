package com.project.easyBuild.user.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.user.dto.ReviewDto;

@Repository
public class ReviewDaoImpl implements ReviewDao {
	private static final int AUTH_ID = 2; //관리자

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RowMapper<ReviewDto> reviewRowMapper = (rs, rowNum) -> {
		ReviewDto review = new ReviewDto();

		// 리뷰
		review.setReviewId(rs.getInt("review_id"));
		review.setProductId(rs.getInt("product_id"));
		review.setOrderId(rs.getInt("order_id"));
		review.setUserId(rs.getString("user_id"));
		review.setAuthId(rs.getInt("auth_id"));
		review.setTitle(rs.getString("title"));
		review.setContent(rs.getString("content"));
		review.setDate(rs.getDate("date"));
		review.setRating(rs.getInt("rating"));
		review.setIsDeleted(rs.getString("is_deleted"));

		// 상품
		review.setProductName(rs.getString("P_NAME"));

		return review;
	};

	@Override
	public List<ReviewDto> writtenListAll(String userId) {
		String sql = "SELECT r.*, p.P_NAME FROM REVIEW r "
				+ " JOIN PRODUCT p ON r.PRODUCT_ID = p.PRODUCT_ID "
				+ " WHERE r.USER_ID = ? AND r.IS_DELETED = 'N' " 
				+ " ORDER BY REVIEW_ID DESC ";
		return jdbcTemplate.query(sql, new Object[] { userId }, reviewRowMapper);
	}

	@Override
	public List<ReviewDto> writeListAll(String userId) {
		String sql = "SELECT o.ORDER_ID, p.P_NAME FROM ORDER_TABLE o "
				+ " JOIN PRODUCT p ON o.PRODUCT_ID = p.PRODUCT_ID "
				+ " WHERE o.USER_ID = ? "
				+ " AND NOT EXISTS ( "
				+ " 	SELECT 1 FROM REVIEW r "
				+ "		WHERE r.ORDER_ID = o.ORDER_ID "
				+ "   	AND r.PRODUCT_ID = o.PRODUCT_ID "
				+ "	) "
				+ " ORDER BY ORDER_ID DESC ";
		return jdbcTemplate.query(sql, new Object[] { userId }, (rs, rowNum) -> {
			ReviewDto dto = new ReviewDto();
			dto.setOrderId(rs.getInt("ORDER_ID"));
			dto.setProductName(rs.getString("P_NAME"));
			return dto;
		});
	}

	@Override
	public List<ReviewDto> listAll() {
		String sql = "SELECT r.*, p.P_NAME FROM REVIEW r "
	+ " JOIN PRODUCT p ON r.PRODUCT_ID = p.PRODUCT_ID ";
		return jdbcTemplate.query(sql, reviewRowMapper);
	}

	@Override
	public ReviewDto listOne(int reviewId, String userId) {
		System.out.println(reviewId + " " + userId);
		String sql = "SELECT r.*, p.P_NAME FROM REVIEW r "
				+ " JOIN PRODUCT p ON r.PRODUCT_ID = p.PRODUCT_ID "
				+ " WHERE r.REVIEW_ID = ? AND r.USER_ID = ? ";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { reviewId, userId }, reviewRowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null; // 데이터 못 찾았을 때
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("REVIEW 조회 중 오류가 발생했습니다.");
		}
	}

	@Override
	public int update(ReviewDto dto) {
		String sql = "UPDATE REVIEW SET TITLE = ?, CONTENT = ?, RATING = ? WHERE REVIEW_ID = ? AND USER_ID = ?";

		return jdbcTemplate.update(sql, dto.getTitle(), dto.getContent(), dto.getRating(), dto.getReviewId(),
				dto.getUserId());
	}

	@Override
	public int delete(int reviewId, String userId) {
		String sql = "UPDATE REVIEW SET IS_DELETED = 'Y' WHERE REVIEW_ID = ? AND USER_ID = ?";

		return jdbcTemplate.update(sql, reviewId, userId);
	}

	@Override
	public int insert(ReviewDto dto) {
		String sql = "INSERT INTO REVIEW "
				+ "VALUES (SEQ_REVIEW.NEXTVAL, ?, ?, ?, ?, ?, ?, SYSDATE, ?, 'N')";

		return jdbcTemplate.update(sql, getProductId(dto.getOrderId()), dto.getOrderId(), dto.getUserId(), 1,
				dto.getTitle(), dto.getContent(), dto.getRating());
	}

	private int getProductId(int orderId) {
		String sql = "SELECT PRODUCT_ID FROM ORDER_TABLE WHERE ORDER_ID = ?";
		return jdbcTemplate.queryForObject(sql, Integer.class, orderId);
	}
}
