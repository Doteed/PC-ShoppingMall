package com.project.easyBuild.entire.biz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.easyBuild.entire.dao.OrderDao;
import com.project.easyBuild.entire.dto.OrderDto;
import com.project.easyBuild.user.dto.OrderRequestDto;

@Service
public class OrderBizImpl implements OrderBiz {
	@Autowired
	private OrderDao  dao;

	@Override
	public List<OrderDto> mylistAll(String userId) {
		return dao.mylistAll(userId);
	}

	@Override
	public List<OrderDto> listAll() {
		return dao.listAll();
	}

	@Override
	public OrderDto listOne(int orderId, String userId) {
		return dao.listOne(orderId, userId);
	}

	@Override
	public int update(OrderDto dto, String userId) {
		return dao.update(dto, userId);
	}

    @Override
    public int update(int deliveryId, String deliveryStatus) {
        return dao.update(deliveryId, deliveryStatus);
    }
    
	@Override
	public int cancle(int orderId, String userId, int authId) {
		return dao.cancle(orderId, userId, authId);
	}

	@Override
	public Map<String, Integer> count(String userId) {
		return dao.count(userId);
	}

	@Override
	public int insertFromCart(OrderRequestDto dto) {
		return dao.insertFromCart(dto);
	}
	
	//관리자 주문 수정
	@Override
	@Transactional
	public int updateOrder(OrderDto dto, String userId) {
	    return dao.updateOrder(dto, userId);
	}

}
