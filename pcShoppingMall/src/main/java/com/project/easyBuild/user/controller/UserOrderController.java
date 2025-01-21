package com.project.easyBuild.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.easyBuild.entire.biz.OrderBiz;
import com.project.easyBuild.entire.dto.OrderDto;
import com.project.easyBuild.member.dto.MemberDto;
import com.project.easyBuild.user.dto.OrderRequestDto;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/order")
public class UserOrderController {
	@Autowired
	private OrderBiz orderbiz;

	@GetMapping("/detail/{orderId}")
	public ResponseEntity<?> getDetail(@PathVariable int orderId, HttpSession session) {
		MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
		try {
			System.out.println(user.getUserId());
			OrderDto order = orderbiz.listOne(orderId, user.getUserId());

			if (order != null) {
	            Map<String, Object> response = new HashMap<>();
	            response.put("userId", user.getUserName());
	            System.out.println(user.getPhone());
	            response.put("phone", user.getPhone());
	            response.put("order", order);
	            return ResponseEntity.ok(response);

				//return ResponseEntity.ok(order);
			} else {
				 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("잘못된 접근입니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/insertFromCart")
	public ResponseEntity<?> insertFromCart(@RequestBody OrderRequestDto orderRequestDto, HttpSession session) {
		System.out.println("OrderRequestDto: " + orderRequestDto);
		MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }

	    orderRequestDto.setUserId(user.getUserId());
	    orderRequestDto.setAuthId(user.getAuthId());
	    System.out.println("OrderRequestDto: " + orderRequestDto);
	    try {
	        int result = orderbiz.insertFromCart(orderRequestDto);
	        return result > 0 ?
	                ResponseEntity.ok(Map.of("success", true, "message", "주문이 성공적으로 처리되었습니다.")) :
	                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "주문 처리가 실패하였습니다."));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", e.getMessage()));
	    }
	}
	
	@PostMapping("/insertFromProduct")
	public ResponseEntity<?> insertFromProduct(@RequestBody OrderRequestDto orderRequestDto, HttpSession session) {
		System.out.println("Product - OrderRequestDto: " + orderRequestDto);
		MemberDto user = (MemberDto) session.getAttribute("dto");
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }

	    orderRequestDto.setUserId(user.getUserId());
	    orderRequestDto.setAuthId(user.getAuthId());
	    System.out.println("Product - OrderRequestDto: " + orderRequestDto);
	    try {
	        int result = orderbiz.insertFromProduct(orderRequestDto);
	        return result > 0 ?
	                ResponseEntity.ok(Map.of("success", true, "message", "주문이 성공적으로 처리되었습니다.")) :
	                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "주문 처리가 실패하였습니다."));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", e.getMessage()));
	    }
	}

	@PutMapping("/update") // 주소 변경
	public ResponseEntity<?> myUpdate(@RequestBody OrderDto orderDto, HttpSession session) {
		MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
		int result = orderbiz.update(orderDto, user.getUserId());

		Map<String, Object> response = new HashMap<>();

		System.out.println("update");
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
	public ResponseEntity<Map<String, String>> cancle(@RequestParam int orderId,  HttpSession session) {
		System.out.println("cancle");
		MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
		int result = orderbiz.cancle(orderId, user.getUserId(), user.getAuthId());

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
	public ResponseEntity<?> getCount(HttpSession session) {
		MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
		try {
			Map<String, Integer> statusCount = orderbiz.count(user.getUserId());
			return ResponseEntity.ok(statusCount);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
