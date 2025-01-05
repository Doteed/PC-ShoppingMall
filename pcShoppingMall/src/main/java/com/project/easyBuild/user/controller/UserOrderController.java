package com.project.easyBuild.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.easyBuild.entire.biz.OrderBiz;
import com.project.easyBuild.entire.dto.OrderDto;

@RestController
@RequestMapping("/order")
public class UserOrderController {
	@Autowired
	private OrderBiz orderbiz;

	@GetMapping("/detail/{orderId}")
	public ResponseEntity<OrderDto> getDetail(@PathVariable int orderId, @RequestParam String userId) {
		try {
			OrderDto order = orderbiz.listOne(orderId, userId);

			if (order != null) {
				return ResponseEntity.ok(order);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/update") // 주소 변경
	public ResponseEntity<Map<String, Object>> myUpdate(@RequestBody OrderDto orderDto) {
		System.out.println("받은 OrderDto: " + orderDto);

		int result = orderbiz.myUpdate(orderDto);
		System.out.println("업데이트 결과: " + result);

		Map<String, Object> response = new HashMap<>();

		if (result > 0) {
			response.put("success", true);
			response.put("message", "해당 주문이 성공적으로 수정되었습니다.");
			return ResponseEntity.ok(response);
		} else {
			response.put("success", false);
			response.put("message", "해당 주문 수정을 실패하였습니다.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	// 취소
	@PutMapping("/cancle")
	public ResponseEntity<Map<String, String>> cancle(@RequestParam int orderId, @RequestParam String userId) {// @RequestAttribute("userId")
		int result = orderbiz.cancle(orderId, userId);

		Map<String, String> response = new HashMap<>();
		if (result > 0) {
			response.put("message", "해당 주문이 성공적으로 취소되었습니다.");
			return ResponseEntity.ok(response);
		} else {
			response.put("message", "해당 주문 취소를 실패하였습니다.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	// 주문 상태 카운팅
	@GetMapping("/count")
	public ResponseEntity<Map<String, Integer>> getCount(@RequestParam String userId) {
		try {
			System.out.println("userId: " + userId); // userId 로그 출력
			Map<String, Integer> statusCount = orderbiz.count(userId);
			return ResponseEntity.ok(statusCount);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
