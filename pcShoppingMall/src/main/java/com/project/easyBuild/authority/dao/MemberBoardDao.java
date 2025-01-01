package com.project.easyBuild.authority.dao;

import org.springframework.data.domain.Pageable;

import com.project.easyBuild.authority.dto.MemberBoardDto;

import java.util.List;

public interface MemberBoardDao {
    List<MemberBoardDto> listAllWithPagination(Pageable pageable);

    long countMembers();

    MemberBoardDto findById(String userId);
    
    void deleteById(String userId);

}

