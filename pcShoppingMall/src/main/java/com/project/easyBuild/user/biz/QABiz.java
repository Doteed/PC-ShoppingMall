package com.project.easyBuild.user.biz;

import java.util.List;

import com.project.easyBuild.user.dto.QADto;

public interface QABiz {
	public List<QADto> mylistAll(String userId);
	public List<QADto> listAll();
	public QADto listOne(int qaId);
}
