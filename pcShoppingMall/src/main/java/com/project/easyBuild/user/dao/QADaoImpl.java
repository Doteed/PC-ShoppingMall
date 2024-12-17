package com.project.easyBuild.user.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.user.dto.QADto;
import com.project.easyBuild.user.dto.ReviewDto;

@Repository
public class QADaoImpl implements QADao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<QADto> rowMapper = (rs, rowNum) -> {
	    QADto qa = new QADto();
	    
	    qa.setQaId(rs.getInt("qa_id"));
	    qa.setBoardId(rs.getInt("board_id"));
	    qa.setUserId(rs.getString("user_id"));
	    qa.setAuthId(rs.getInt("auth_id"));
	    qa.setTitle(rs.getString("title"));
	    qa.setContent(rs.getString("content"));
	    qa.setDate(rs.getDate("date"));
	    
	    return qa;
	};
	
	@Override
	public List<QADto> mylistAll(String userId){
		String sql = "SELECT * FROM QA " +
				" WHERE USERID = ? ";
		return jdbcTemplate.query(sql, new Object[]{userId}, rowMapper);
	}

	@Override
	public List<QADto> listAll() {
		String sql = "SELECT * FROM QA ";
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public QADto listOne(int qaId) {
		String sql = "SELECT * FROM QA " +
				" WHERE QAID = ? ";
		return jdbcTemplate.queryForObject(sql, new Object[]{qaId}, rowMapper);
	}
}
