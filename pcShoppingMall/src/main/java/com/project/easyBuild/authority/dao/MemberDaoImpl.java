package com.project.easyBuild.authority.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.authority.dto.MemberDto;
@Repository
public class MemberDaoImpl implements MemberDao {

    private final JdbcTemplate jdbcTemplate;

    public MemberDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<MemberDto> rowMapper = (rs, rowNum) -> {
        MemberDto member = new MemberDto();
        member.setMemberNo(rs.getInt("MEMBER_NO"));
        member.setUserId(rs.getString("USER_ID"));
        member.setAuthId(rs.getInt("AUTH_ID"));
        member.setPassword(rs.getString("PASSWORD"));
        member.setUsername(rs.getString("USERNAME"));
        member.setGender(rs.getString("GENDER"));
        member.setEmail(rs.getString("EMAIL"));
        member.setPhone(rs.getString("PHONE"));
        member.setRegisterDate(rs.getTimestamp("REGISTER_DATE").toLocalDateTime());
        member.setLastUpdate(rs.getTimestamp("LAST_UPDATE").toLocalDateTime());
        member.setPurchaseCount(rs.getInt("PURCHASE_COUNT"));
        member.setMemberStatus(rs.getString("MEMBER_STATUS"));
        return member;
    };

    @Override
    public List<MemberDto> listAllWithPagination(Pageable pageable) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        int pageSize = pageable.getPageSize();

        String sql = "SELECT * FROM MEMBER ORDER BY REGISTER_DATE DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        List<MemberDto> members = jdbcTemplate.query(sql, rowMapper, offset, pageSize);

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
    public MemberDto findById(String userId) {
        String sql = "SELECT * FROM MEMBER WHERE USER_ID = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, userId);
    }
    
    @Override
    public void deleteById(String userId) {
        String sql = "DELETE FROM MEMBER WHERE USER_ID = ?";
        jdbcTemplate.update(sql, userId);
    }

    
}
