package com.project.easyBuild.user.dao;

import java.util.List;

import com.project.easyBuild.user.dto.ReviewDto;

public interface ReviewDao {
	public List<ReviewDto> mylistAll(String userId);
	public List<ReviewDto> listAll();
}
