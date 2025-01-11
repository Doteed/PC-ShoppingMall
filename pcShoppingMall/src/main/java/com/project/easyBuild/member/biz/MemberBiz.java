package com.project.easyBuild.member.biz;

import com.project.easyBuild.member.dto.MemberDto;

public interface MemberBiz {
	public MemberDto login(MemberDto dto);
	
	public int insert(MemberDto dto);
	
	public int delete(String userId);
	
	public MemberDto selectOne(String userId);
	
	public int update(MemberDto dto);
	
	public MemberDto find_id(MemberDto dto);
	
	public void findPw(String email,String userId)throws Exception;

	public int findPwCheck(MemberDto dto)throws Exception;
}
