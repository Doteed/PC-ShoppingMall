package com.project.easyBuild.authority.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.easyBuild.authority.dto.MemberDto;

@Service
public interface MemberBiz {
    Page<MemberDto> listAllWithPagination(Pageable pageable);

    MemberDto getMemberById(String userId);
    
    void deleteMember(String userId);

}
