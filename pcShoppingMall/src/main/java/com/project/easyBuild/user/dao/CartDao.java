package com.project.easyBuild.user.dao;

import java.util.List;

import com.project.easyBuild.user.dto.CartDto;

public interface CartDao {
	public List<CartDto> mylistAll(String userId);
	public int insert(int productId, int quantity, String userId);
	public int update(List<Integer> cartIds, List<Integer> quantities, String userId);
	public int update(String userId, List<Integer> cartIds, List<String> selecteds);
	public int delete(List<Integer> cartIds, String userId);
    public int deleteAll(String userId);
	public boolean isCartNotEmpty(String userId);
}
