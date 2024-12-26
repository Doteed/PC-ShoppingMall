package com.project.easyBuild.user.dao;

import java.util.List;

import com.project.easyBuild.user.dto.QADto;

public interface QADao {
	public List<QADto> mylistAll(String userId);
	public List<QADto> listAll();
	public QADto listOne(int qaId, String userId);
	public int update(QADto dto);
	public int delete(int qaId, String userId);
}
