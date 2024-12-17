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
	public List<ReviewDto> mylistAll(String userId) {
		return dao.mylistAll(userId);
	}
}
