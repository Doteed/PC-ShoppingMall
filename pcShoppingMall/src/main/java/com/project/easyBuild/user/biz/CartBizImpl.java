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
		System.out.println("Fetching cart items for userId=" + userId);
		return dao.mylistAll(userId);
	}
	
	@Override
	public int insert(int productId, String productType, int quantity, String userId) {
		System.out.println("Inserting to cart: productId=" + productId + ", quantity=" + quantity + ", userId=" + userId);
		return dao.insert(productId, productType, quantity, userId);
	}
	
	@Override
	public int update(int cartId, int quantity, String userId) {
	    return dao.update(cartId, quantity, userId);
	}

	@Override
	public int delete(List<Integer> cartIds, String userId) {
		return dao.delete(cartIds, userId);
	}
	
    @Override
    public int deleteAll(String userId) {
        return dao.deleteAll(userId);
    }

    @Override
    public int getCartItemCount(String userId) {
    	return dao.getCartItemCount(userId);
    }
}
