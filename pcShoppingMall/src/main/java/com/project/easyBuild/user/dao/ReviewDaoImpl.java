package com.project.easyBuild.user.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.product.dto.ProductDto;
import com.project.easyBuild.user.dto.ReviewDto;

@Repository
public class ReviewDaoImpl implements ReviewDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<ReviewDto> rowMapper = (rs, rowNum) -> {
	    ReviewDto review = new ReviewDto();
	    
	    //리뷰
	    review.setReviewId(rs.getInt("review_id"));
	    review.setProductId(rs.getInt("product_id"));
	    review.setUserId(rs.getString("user_id"));
	    review.setAuthId(rs.getInt("auth_id"));
	    review.setTitle(rs.getString("title"));
	    review.setContent(rs.getString("content"));
	    review.setDate(rs.getDate("date"));
	    review.setRating(rs.getInt("rating"));
	    
	    //상품
	    review.setProductName(rs.getString("P_NAME"));
	  
	    return review;
	};
	
	@Override
	public List<ReviewDto> mylistAll(String userId){
		String sql = "SELECT r.*, p.P_NAME " +
				" FROM REVIEW r " +
				" JOIN PRODUCT p ON r.PRODUCT_ID = p.PRODUCT_ID " +
				" WHERE r.USERID = ? ";
		return jdbcTemplate.query(sql, new Object[]{userId}, rowMapper);
	}

	@Override
	public List<ReviewDto> listAll() {
		String sql = "SELECT r.*, p.P_NAME " +
				" FROM REVIEW r " +
				" JOIN PRODUCT p ON r.PRODUCT_ID = p.PRODUCT_ID ";
		return jdbcTemplate.query(sql, rowMapper);
	}
}
