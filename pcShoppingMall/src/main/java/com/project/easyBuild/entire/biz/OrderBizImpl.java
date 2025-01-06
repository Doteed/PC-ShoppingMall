package com.project.easyBuild.entire.biz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.entire.dao.OrderDao;
import com.project.easyBuild.entire.dto.OrderDto;

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
	public int myUpdate(OrderDto dto) {
		return dao.myUpdate(dto);
	}

	@Override
	public int cancle(int orderId, String userId) {
		return dao.cancle(orderId, userId);
	}

	@Override
	public Map<String, Integer> count(String userId) {
		return dao.count(userId);
	}
	
	 @Override
    public int insert(OrderDto order) {
        return dao.insert(order);
    }
}
