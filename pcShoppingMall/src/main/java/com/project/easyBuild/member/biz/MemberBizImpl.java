package com.project.easyBuild.member.biz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.member.dao.MemberDao;
import com.project.easyBuild.member.dto.MemberDto;

@Service
public class MemberBizImpl implements MemberBiz {

    private static final Logger logger = LoggerFactory.getLogger(MemberBizImpl.class);

    @Autowired
    private MemberDao dao;

    @Override
    public MemberDto login(MemberDto dto) {
        logger.debug("Attempting login for user: {}", dto.getUserId());
        return dao.login(dto);
    }

    @Override
    public int insert(MemberDto dto) {
        logger.debug("Inserting user: {}", dto.getUserId());
        // Use the boolean result directly
        return dao.insert(dto);  // 아이디가 중복되지 않으면 회원가입 처리
    }

    @Override
    public int delete(String userId) {
        logger.debug("Deleting user: {}", userId);
        return dao.delete(userId);
    }

}
