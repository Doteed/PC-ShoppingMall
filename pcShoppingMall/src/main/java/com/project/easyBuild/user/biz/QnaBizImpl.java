package com.project.easyBuild.user.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.user.dao.QnaDao;
import com.project.easyBuild.user.dto.QnaDto;


@Service
public class QnaBizImpl implements QnaBiz {
	@Autowired
	private QnaDao dao;
	
	public List<QnaDto> listAll() {
		return dao.listAll();
	}

	@Override
	public List<QnaDto> mylistAll(String userId) {
		return dao.mylistAll(userId);
	}

	@Override
	public QnaDto listOne(int qnaId, String userId) {
		return dao.listOne(qnaId, userId);
	}

	@Override
	public int update(QnaDto dto) {
		return dao.update(dto);
	}

	@Override
	public int delete(int qnaId, String userId) {
		return dao.delete(qnaId, userId);
	}
}
