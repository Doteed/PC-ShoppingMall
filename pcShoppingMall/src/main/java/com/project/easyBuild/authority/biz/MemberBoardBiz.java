package com.project.easyBuild.authority.biz;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.easyBuild.authority.dto.MemberBoardDto;

@Service
public interface MemberBoardBiz {
    Page<MemberBoardDto> listAllWithPagination(Pageable pageable);

    MemberBoardDto getMemberById(String userId);
    
    void deleteMember(String userId);

}
