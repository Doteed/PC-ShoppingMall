package com.project.easyBuild.user.dao;

import java.util.List;

import com.project.easyBuild.user.dto.QnaDto;

public interface QnaDao {
	public List<QnaDto> mylistAll(String userId);
	public List<QnaDto> listAll();
	public QnaDto listOne(int qnaId, String userId);
	public int update(QnaDto dto);
	public int delete(int qnaId, String userId);
}
