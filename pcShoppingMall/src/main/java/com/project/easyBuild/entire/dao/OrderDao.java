package com.project.easyBuild.entire.dao;

import java.util.List;
import java.util.Map;

import com.project.easyBuild.entire.dto.OrderDto;
import com.project.easyBuild.user.dto.OrderRequestDto;

public interface OrderDao {
	public List<OrderDto> mylistAll(String userId);
	public List<OrderDto> listAll();
	public OrderDto listOne(int orderId, String userId);
	public int update(OrderDto dto, String userId);
    public int update(int deliveryId, String deliveryStatus);
	public int cancle(int orderId, String userId, int authId);
	public Map<String, Integer> count(String userId);
	public int insert(OrderDto dto);
	public int insertFromCart(OrderRequestDto dto);
}
