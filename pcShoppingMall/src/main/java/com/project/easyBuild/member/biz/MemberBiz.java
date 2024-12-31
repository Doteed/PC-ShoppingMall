package com.project.easyBuild.member.biz;

import com.project.easyBuild.member.dto.MemberDto;

public interface MemberBiz {
	public MemberDto login(MemberDto dto);
	
	public int insert(MemberDto dto);
	
	public int delete(String userId);
	
}
