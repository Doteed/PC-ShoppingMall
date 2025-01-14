package com.project.easyBuild.entire.biz;

import java.util.List;
import java.util.Map;

import com.project.easyBuild.entire.dto.OrderDto;
import com.project.easyBuild.user.dto.OrderRequestDto;

public interface OrderBiz {
	public List<OrderDto> mylistAll(String userId);
	public List<OrderDto> listAll();
	public OrderDto listOne(int orderId, String userId);
	public int update(OrderDto dto, String userId);
    public int update(int deliveryId, String deliveryStatus);
	public int cancle(int orderId, String userId, int authId);
	public Map<String, Integer> count(String userId);
	public int insertFromCart(OrderRequestDto dto);
	public int updateOrder(OrderDto dto, String userId);
}
