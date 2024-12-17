package com.project.easyBuild.user.biz;

import java.util.List;

import com.project.easyBuild.user.dto.ReviewDto;


public interface ReviewBiz {
	public List<ReviewDto> mylistAll(String userId);
	public List<ReviewDto> listAll();
}
