package com.project.easyBuild.member.dao;

import java.util.List;

import com.project.easyBuild.member.dto.MemberDto;

public interface MemberDao {
	public List<MemberDto> listAll();
	
	public MemberDto login(MemberDto dto);
	
	public int insert(MemberDto dto);	
	
	public int delete(String userId);
	
	public MemberDto selectOne(String userId);
	
	public int update(MemberDto dto);
	
	public MemberDto find_id(MemberDto dto);
	
	public int findPwCheck(MemberDto dto)throws Exception; 
	
	public int findPw(String password,String email,String userId)throws Exception;
}
