package com.project.easyBuild.user.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.user.dto.QADto;

@Repository
public class QADaoImpl implements QADao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final int authId = 1; //관리자 권한

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
		String sql = "SELECT * FROM QA WHERE USER_ID = ? ";
		return jdbcTemplate.query(sql, new Object[]{userId}, rowMapper);
	}

	@Override
	public List<QADto> listAll() {
		String sql = "SELECT * FROM QA ";
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public QADto listOne(int qaId, String userId) {
	    String sql = "SELECT * FROM QA WHERE QA_ID = ? AND USER_ID = ?";
	    try {
	        return jdbcTemplate.queryForObject(sql, new Object[]{qaId, userId}, rowMapper);
	    } catch (EmptyResultDataAccessException e) {
	        return null; //데이터 못 찾았을 때
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Q&A 조회 중 오류가 발생했습니다.");
	    }
	}


	@Override
	public int update(QADto dto) {
	    String sql = "UPDATE QA SET TITLE = ?, CONTENT = ? WHERE QA_ID = ? AND (USER_ID = ? OR AUTH_ID = ?)";
	    
	    return jdbcTemplate.update(sql, dto.getTitle(), dto.getContent(), dto.getQaId(), dto.getUserId(), authId);
	}

	@Override
	public int delete(int qaId, String userId) {
	    String sql = "DELETE FROM QA WHERE QA_ID = ? AND (USER_ID = ? OR AUTH_ID = ?)";
	    
	    return jdbcTemplate.update(sql, qaId, userId, authId);
	}

}
