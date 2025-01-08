package com.project.easyBuild.user.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.easyBuild.user.dao.ReviewDao;
import com.project.easyBuild.user.dto.ReviewDto;

@Service
public class ReviewBizImpl implements ReviewBiz {
	@Autowired
	private ReviewDao dao;
	
	public List<ReviewDto> listAll() {
		return dao.listAll();
	}

	@Override
	public List<ReviewDto> writtenListAll(String userId) {
		return dao.writtenListAll(userId);
	}
	
	@Override
	public List<ReviewDto> writeListAll(String userId) {
		return dao.writeListAll(userId);
	}

	@Override
	public ReviewDto listOne(int reviewId, String userId) {
		return dao.listOne(reviewId, userId);
	}

	@Override
	public int update(ReviewDto dto) {
		return dao.update(dto);
	}

	@Override
	public int insert(ReviewDto dto) {
		return dao.insert(dto);
	}
	
	@Override
	public int delete(int reviewId, String userId) {
		return dao.delete(reviewId, userId);
	}


}
