package com.project.easyBuild.user.biz;

import java.util.List;

import com.project.easyBuild.user.dto.OrderDto;

public interface OrderBiz {
	public List<OrderDto> mylistAll(String userId);
	public List<OrderDto> listAll();
	public OrderDto listOne(int orderId, String userId);
	public int update(OrderDto dto);
	public int delete(int orderId, String userId);
}
