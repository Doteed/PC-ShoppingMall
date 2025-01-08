package com.project.easyBuild.user.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.user.dao.CartDao;
import com.project.easyBuild.user.dto.CartDto;


@Service
public class CartBizImpl implements CartBiz {
	@Autowired
	private CartDao dao;
	
	@Override
	public List<CartDto> mylistAll(String userId) {
		return dao.mylistAll(userId);
	}

	@Override
	public int delete(List<Integer> cartIds, String userId) {
		return dao.delete(cartIds, userId);
	}
	
	@Override
	public int insert(int productId, int quantity, String userId) {
		return dao.insert(productId, quantity, userId);
	}
	
	@Override
	public int update(List<Integer> cartIds, List<Integer> quantities, String userId) {
	    return dao.update(cartIds, quantities, userId);
	}

	@Override
	public int update(String userId, List<Integer> cartIds, List<String> selecteds) {
		return dao.update(userId, cartIds, selecteds);
	}
}
