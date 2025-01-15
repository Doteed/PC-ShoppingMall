package com.project.easyBuild.member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.easyBuild.member.dto.MemberDto;

@Repository
public class MemberDaoImpl implements MemberDao {
    private static final Logger logger = LoggerFactory.getLogger(MemberDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<MemberDto> listAll() {
        String sql = "SELECT * FROM MEMBER";
        logger.debug("Executing query: {}", sql);
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MemberDto member = new MemberDto();
            member.setUserId(rs.getString("USER_ID"));
            member.setAuthId(rs.getInt("AUTH_ID"));
            member.setPassword(rs.getString("PASSWORD"));
            member.setUserName(rs.getString("USERNAME"));
            member.setGender(rs.getString("GENDER"));
            member.setEmail(rs.getString("EMAIL"));
            member.setPhone(rs.getString("PHONE"));
            member.setRegisterDate(rs.getDate("REGISTER_DATE"));
            member.setLastUpdate(rs.getDate("LAST_UPDATE"));
            member.setMemberStatus(rs.getString("MEMBER_STATUS"));
            return member;
        });
    }

    private final String LOGIN_QUERY = "SELECT * FROM MEMBER WHERE USER_ID = ? AND PASSWORD = ?";

    @Override
    public MemberDto login(MemberDto dto) {
        MemberDto res = null;
        logger.debug("Attempting login for user: {}", dto.getUserId());

        try {
            res = jdbcTemplate.query(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(LOGIN_QUERY);
                    ps.setString(1, dto.getUserId());
                    ps.setString(2, dto.getPassword());
                    return ps;
                },
                (rs, rowNum) -> {
                    MemberDto member = new MemberDto();
                    member.setUserId(rs.getString("USER_ID"));
                    member.setAuthId(rs.getInt("AUTH_ID")); // authId 매핑 추가
                    member.setPassword(rs.getString("PASSWORD"));
                    member.setUserName(rs.getString("USERNAME"));
                    member.setGender(rs.getString("GENDER"));
                    member.setEmail(rs.getString("EMAIL"));
                    member.setPhone(rs.getString("PHONE"));
                    member.setRegisterDate(rs.getDate("REGISTER_DATE"));
                    member.setLastUpdate(rs.getDate("LAST_UPDATE"));
                    member.setMemberStatus(rs.getString("MEMBER_STATUS"));
                    return member;
                }
            ).stream().findFirst().orElse(null);
        } catch (Exception e) {
            logger.error("[error] : login", e);
        }

        return res;
    }


    @Override
    public int insert(MemberDto dto) {
        String sql = "INSERT INTO MEMBER (USER_ID, AUTH_ID, PASSWORD, USERNAME, GENDER, EMAIL, PHONE, REGISTER_DATE, LAST_UPDATE, MEMBER_STATUS) "
                   + "VALUES (?, 1, ?, ?, ?, ?, ?, CURRENT_DATE, CURRENT_DATE, 'Y')";
        logger.debug("Inserting user: {}", dto.getUserId());
        return jdbcTemplate.update(sql, dto.getUserId(), dto.getPassword(), dto.getUserName(), dto.getGender(), dto.getEmail(), dto.getPhone());
    }

    @Override
    public int delete(String userId) {
        String sql = "DELETE FROM MEMBER WHERE USER_ID = ?";
        logger.debug("Deleting user: {}", userId);
        return jdbcTemplate.update(sql, userId);
    }
    
    @Override
    public MemberDto selectOne(String userId) {
        String sql = "SELECT * FROM MEMBER WHERE USER_ID = ?";
        logger.debug("Executing query: {} with userId={}", sql, userId);
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
            MemberDto member = new MemberDto();
            member.setUserId(rs.getString("USER_ID"));
            member.setAuthId(rs.getInt("AUTH_ID"));
            member.setPassword(rs.getString("PASSWORD"));
            member.setUserName(rs.getString("USERNAME"));
            member.setGender(rs.getString("GENDER"));
            member.setEmail(rs.getString("EMAIL"));
            member.setPhone(rs.getString("PHONE"));
            member.setRegisterDate(rs.getDate("REGISTER_DATE"));
            member.setLastUpdate(rs.getDate("LAST_UPDATE"));
            member.setMemberStatus(rs.getString("MEMBER_STATUS"));
            return member;
        });
    }
    
    @Override
    public int update(MemberDto dto) {
    	int res = 0;
    	String sql = "UPDATE MEMBER SET USERNAME=?,PASSWORD=?,PHONE=? WHERE USER_ID=?";
    	
    	return jdbcTemplate.update(sql,dto.getUserName(),dto.getPassword(),dto.getPhone(),dto.getUserId());
    }

    @Override
    public MemberDto find_id(MemberDto dto) {
        try {
            String sql = "SELECT USER_ID FROM MEMBER WHERE USERNAME = ? AND EMAIL = ?";
            
            // 직접 RowMapper를 구현하여 필요한 필드만 설정
            return jdbcTemplate.queryForObject(sql, new Object[]{dto.getUserName(), dto.getEmail()},
                    (rs, rowNum) -> {
                        MemberDto member = new MemberDto();
                        member.setUserId(rs.getString("USER_ID")); // USER_ID만 설정
                        return member;
                    });
        } catch (EmptyResultDataAccessException e) {
            return null; // 회원이 없으면 null 반환
        }
    }

    @Override
    public int findPwCheck(MemberDto dto) throws Exception {
        String sql = "SELECT COUNT(*) FROM MEMBER WHERE USER_ID = ? AND EMAIL = ?";
        logger.debug("Checking password reset eligibility for user: {}", dto.getUserId());
        return jdbcTemplate.queryForObject(sql, new Object[]{dto.getUserId(), dto.getEmail()}, Integer.class);
    }

    @Override
    public int findPw(String password, String email, String userId) throws Exception {
        String sql = "UPDATE MEMBER SET PASSWORD = ? WHERE EMAIL = ? AND USER_ID = ?";
        logger.debug("Updating password for userId={} and email={}", userId, email);
        return jdbcTemplate.update(sql, password, email, userId);
    }

}
