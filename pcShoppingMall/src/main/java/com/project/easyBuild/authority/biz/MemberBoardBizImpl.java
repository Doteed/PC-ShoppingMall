package com.project.easyBuild.authority.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.easyBuild.authority.dao.MemberBoardDao;
import com.project.easyBuild.authority.dto.MemberBoardDto;

@Service
public class MemberBoardBizImpl implements MemberBoardBiz {

    @Autowired
    private MemberBoardDao memberDao;

    @Override
    public Page<MemberBoardDto> listAllWithPagination(Pageable pageable) {
        List<MemberBoardDto> members = memberDao.listAllWithPagination(pageable);
        long total = memberDao.countMembers();
        return new PageImpl<>(members, pageable, total);
    }

    @Override
    public MemberBoardDto getMemberById(String userId) {
        return memberDao.findById(userId);
    }
    
    @Override
    public void deleteMember(String userId) {
        memberDao.deleteById(userId);
    }

}
