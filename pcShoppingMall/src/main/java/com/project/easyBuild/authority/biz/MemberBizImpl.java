package com.project.easyBuild.authority.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.easyBuild.authority.dao.MemberDao;
import com.project.easyBuild.authority.dto.MemberDto;

@Service
public class MemberBizImpl implements MemberBiz {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Page<MemberDto> listAllWithPagination(Pageable pageable) {
        List<MemberDto> members = memberDao.listAllWithPagination(pageable);
        long total = memberDao.countMembers();
        return new PageImpl<>(members, pageable, total);
    }

    @Override
    public MemberDto getMemberById(String userId) {
        return memberDao.findById(userId);
    }
    
    @Override
    public void deleteMember(String userId) {
        memberDao.deleteById(userId);
    }

}
