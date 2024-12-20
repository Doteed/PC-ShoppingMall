package com.project.easyBuild.user.biz;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.easyBuild.user.dao.OrderDao;
import com.project.easyBuild.user.dto.OrderDto;

@Service
public class OrderBizImpl implements OrderBiz {
	@Autowired
	private OrderDao dao;

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
	public int update(OrderDto dto) {
		return 0;
	}

	@Override
	public int delete(int orderId, String userId) {
		return dao.delete(orderId, userId);
	}
}
