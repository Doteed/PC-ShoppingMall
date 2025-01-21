package com.project.easyBuild.user.biz;

import java.util.List;

import com.project.easyBuild.user.dto.CartDto;

public interface CartBiz {
	public List<CartDto> mylistAll(String userId);
	public int insert(int productId, String productType, int quantity, String userId);
	public int update(int cartId, int quantity, String userId);
	public int delete(List<Integer> cartIds, String userId);
    public int deleteAll(String userId);
	public int getCartItemCount(String userId);
}
