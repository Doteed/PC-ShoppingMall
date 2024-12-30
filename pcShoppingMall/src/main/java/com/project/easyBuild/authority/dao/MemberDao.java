package com.project.easyBuild.authority.dao;

import org.springframework.data.domain.Pageable;

import com.project.easyBuild.authority.dto.MemberDto;

import java.util.List;

public interface MemberDao {
    List<MemberDto> listAllWithPagination(Pageable pageable);

    long countMembers();

    MemberDto findById(String userId);
    
    void deleteById(String userId);

}

