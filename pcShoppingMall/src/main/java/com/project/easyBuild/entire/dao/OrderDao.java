package com.project.easyBuild.entire.dao;

import java.util.List;
import java.util.Map;

import com.project.easyBuild.entire.dto.OrderDto;

public interface OrderDao {
	public List<OrderDto> mylistAll(String userId);
	public List<OrderDto> listAll();
	public OrderDto listOne(int orderId, String userId);
	public int myUpdate(OrderDto dto);
	public int cancle(int orderId, String userId);
	public Map<String, Integer> count(String userId);
}
