package com.project.easyBuild.user.dao;

import java.util.List;

import com.project.easyBuild.user.dto.OrderDto;

public interface OrderDao {
	public List<OrderDto> mylistAll(String userId);
	public List<OrderDto> listAll();
	public OrderDto listOne(int orderId, String userId);
	public int update(OrderDto dto);
	public int delete(int orderId, String userId);
}
