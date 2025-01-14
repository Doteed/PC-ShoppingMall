package com.project.easyBuild.authority.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.authority.dto.MemberBoardDto;
@Repository
public class MemberBoardDaoImpl implements MemberBoardDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberBoardDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberBoardDto> rowMapper = (rs, rowNum) -> {
        MemberBoardDto member = new MemberBoardDto();
        member.setUserId(rs.getString("USER_ID"));
        member.setAuthId(rs.getInt("AUTH_ID"));
        member.setPassword(rs.getString("PASSWORD"));
        member.setUsername(rs.getString("USERNAME"));
        member.setGender(rs.getString("GENDER"));
        member.setEmail(rs.getString("EMAIL"));
        member.setPhone(rs.getString("PHONE"));
        member.setRegisterDate(rs.getTimestamp("REGISTER_DATE").toLocalDateTime()); 
        member.setLastUpdate(rs.getTimestamp("LAST_UPDATE").toLocalDateTime());
        member.setMemberStatus(rs.getString("MEMBER_STATUS"));
        return member;
    };


    @Override
    public List<MemberBoardDto> listAllWithPagination(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int pageSize = pageable.getPageSize();

        // 페이징 처리를 위한 SQL
        String sql = "SELECT * FROM MEMBER ORDER BY REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        // SQL 실행 시 페이징 관련 파라미터 전달
        List<MemberBoardDto> members = jdbcTemplate.query(sql, rowMapper, offset, pageSize);

        // 로그 추가
        System.out.println("DAO fetched members: " + members);

        return members;
    }


    @Override
    public long countMembers() {
        String sql = "SELECT COUNT(*) FROM MEMBER";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public MemberBoardDto findById(String userId) {
        String sql = "SELECT * FROM MEMBER WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }
    
    @Override
    public void deleteById(String userId) {
        String sql = "DELETE FROM MEMBER WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    
}
