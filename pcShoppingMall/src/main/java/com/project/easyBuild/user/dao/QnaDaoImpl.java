package com.project.easyBuild.user.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.user.dto.QnaDto;

@Repository
public class QnaDaoImpl implements QnaDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final int authId = 2; // 관리자 권한

	private RowMapper<QnaDto> rowMapper = (rs, rowNum) -> {
		QnaDto qna = new QnaDto();

		qna.setQnaId(rs.getInt("qna_id"));
		qna.setBoardId(rs.getInt("board_id"));
		qna.setUserId(rs.getString("user_id"));
		qna.setAuthId(rs.getInt("auth_id"));
		qna.setTitle(rs.getString("title"));
		qna.setContent(rs.getString("content"));
		qna.setAnswer(rs.getString("answer"));
		qna.setPassword(rs.getString("password"));
		qna.setIsSecret(rs.getInt("is_secret"));
		qna.setCreatedDate(rs.getDate("created_date"));

		return qna;
	};

	@Override
	public List<QnaDto> mylistAll(String userId) {
		String sql = "SELECT * FROM QNA WHERE USER_ID = ?";

		System.out.println("userId: " + userId);

		return jdbcTemplate.query(sql, new Object[] { userId }, rowMapper);
	}

	@Override
	public List<QnaDto> listAll() {
		String sql = "SELECT * FROM QNA";
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public QnaDto listOne(int qnaId, String userId) {
		String sql = "SELECT * FROM QNA "
				+ " WHERE QNA_ID = ? AND USER_ID = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { qnaId, userId }, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null; // 데이터 못 찾았을 때
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Q&A 조회 중 오류가 발생했습니다.");
		}
	}

	@Override
	public int update(QnaDto dto) {
		String sql = "UPDATE QNA SET TITLE = ?, CONTENT = ? WHERE QNA_ID = ? AND USER_ID = ?";

		return jdbcTemplate.update(sql, dto.getTitle(), dto.getContent(), dto.getQnaId(), dto.getUserId());
	}

	@Override
	public int delete(int qnaId, String userId) {
		String sql = "DELETE FROM QNA WHERE QNA_ID = ? AND (USER_ID = ? OR AUTH_ID = ?)";
		
		return jdbcTemplate.update(sql, qnaId, userId, authId);
	}
}
