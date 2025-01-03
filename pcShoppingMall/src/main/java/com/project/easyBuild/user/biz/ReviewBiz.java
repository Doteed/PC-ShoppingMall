package com.project.easyBuild.user.biz;

import java.util.List;

import com.project.easyBuild.user.dto.ReviewDto;

public interface ReviewBiz {
	public List<ReviewDto> writtenListAll(String userId);
	public List<ReviewDto> writeListAll(String userId);
	public List<ReviewDto> listAll();
	public ReviewDto listOne(int reviewId, String userId);
	public int update(ReviewDto dto);
	public int insert(ReviewDto dto);
	public int delete(int reviewId, String userId);
}
