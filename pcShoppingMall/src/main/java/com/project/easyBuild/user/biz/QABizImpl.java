package com.project.easyBuild.user.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.user.dao.QADao;
import com.project.easyBuild.user.dto.QADto;


@Service
public class QABizImpl implements QABiz {
	@Autowired
	private QADao dao;
	
	public List<QADto> listAll() {
		return dao.listAll();
	}

	@Override
	public List<QADto> mylistAll(String userId) {
		return dao.mylistAll(userId);
	}

	@Override
	public QADto listOne(int qaId) {
		return dao.listOne(qaId);
	}
}
