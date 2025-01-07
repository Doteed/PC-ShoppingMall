package com.project.easyBuild.user.dao;

import java.util.List;

import com.project.easyBuild.user.dto.ReviewDto;

public interface ReviewDao {
	public List<ReviewDto> writtenListAll(String userId);
	public List<ReviewDto> writeListAll(String userId);
	public List<ReviewDto> listAll();
	public ReviewDto listOne(int reviewId, String userId);
	public int update(ReviewDto dto, String userId);
	public int insert(ReviewDto dto, String userId);
	public int delete(int reviewId, String userId, int authId);
}
