package com.project.easyBuild.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.easyBuild.entire.dto.OrderDto;
import com.project.easyBuild.member.dto.MemberDto;
import com.project.easyBuild.user.biz.CartBiz;
import com.project.easyBuild.user.dto.CartDto;
import com.project.easyBuild.user.dto.ReviewDto;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartBiz cartbiz;

//	@PostMapping("/order")
//	public String order(@RequestBody OrderDto orderDto, HttpSession session) {
//	    
//	    MemberDto user = (MemberDto) session.getAttribute("dto");
//
//	    if (user == null) {
//	        return "redirect:/loginform";
//	    }
//
//	    String userId = user.getUserId();
//
//	    List<CartDto> selectedCartItems = cartbiz.getSelectedItems(userId);
//
//	    // OrderDto 리스트로 변환
//	    List<OrderDto> orders = new ArrayList<>();
//	    for (CartDto cart : selectedCartItems) {
//	        OrderDto order = new OrderDto(
//	            orderDto.getPaymentMethod(),
//	            orderDto.getAddressee(),
//	            orderDto.getAddress(),
//	            orderDto.getPhone()
//	        );
//	        order.setUserId(userId);
//	        order.setAuthId(user.getAuthId());
//	        order.setProductId(cart.getProductId());
//	        order.setTotalPrice(cart.getTotalPrice());
//	        orders.add(order);
//	    }
//
//	    // 여러 개의 주문 처리
//	    int result = orderDao.insert(orders);
//
//	    if (result > 0) {
//	        return "orderSuccess"; // 주문 성공 페이지
//	    } else {
//	        return "orderFail"; // 주문 실패 페이지
//	    }
//	}
//
//	@PutMapping("/update")
//	public ResponseEntity<?> update(@RequestBody ReviewDto reviewDto, HttpSession session) {
//		MemberDto user = (MemberDto) session.getAttribute("dto");
//
//		if (user == null) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//					.body(Map.of("redirectUrl", "/loginform"));
//		}
//		
//		int result = cartbiz.update(reviewDto, user.getUserId());
//		Map<String, Object> response = new HashMap<>();
//
//		if (result > 0) {
//			response.put("success", true);
//			response.put("message", "해당 게시글이 성공적으로 수정되었습니다.");
//			return ResponseEntity.ok(response);
//		} else {
//			response.put("success", false);
//			response.put("message", "해당 게시글 수정을 실패하였습니다.");
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//		}
//	}
//
//	@GetMapping("/insert-editor")
//	public ResponseEntity<Map<String, Object>> insertEditor() {
//		Map<String, Object> response = new HashMap<>();
//
//		// 초기화
//		response.put("title", "");
//		response.put("content", "");
//		response.put("rating", 5);
//
//		return ResponseEntity.ok(response);
//	}

	@PostMapping("/insert/{productId}")
	public ResponseEntity<?> insert(@PathVariable("productId") int productId,
			@RequestParam("quantity") int quantity,
			HttpSession session) {

		MemberDto user = (MemberDto) session.getAttribute("dto");

		if (user == null) {
			System.out.println("User not logged in");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		System.out.println("User ID: " + user.getUserId());
		
		int result = cartbiz.insert(productId, quantity, user.getUserId());

		Map<String, Object> response = new HashMap<>();
		if (result > 0) {
			response.put("success", true);
			response.put("message", "장바구니에 상품이 성공적으로 추가되었습니다.");
			return ResponseEntity.ok(response);
		} else {
			response.put("success", false);
			response.put("message", "장바구니 추가에 실패하였습니다.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	//수량 업데이트
	@PutMapping("/update/quantity")
	public ResponseEntity<?> updateQuantity(
			@RequestParam("cartIds") List<Integer> cartIds,
            @RequestParam("quantities") List<Integer> quantities,
            HttpSession session) {
		MemberDto user = (MemberDto) session.getAttribute("dto");

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}

		//명시적 검증
	    if (cartIds.size() != quantities.size()) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", false);
	        response.put("message", "잘못된 입력입니다.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	    
		int result = cartbiz.update(cartIds, quantities, user.getUserId());

		Map<String, Object> response = new HashMap<>();
		if (result > 0) {
			response.put("success", true);
			response.put("message", "수량 업데이트가 성공적으로 처리되었습니다.");
			return ResponseEntity.ok(response);
		} else {
			response.put("success", false);
			response.put("message", "수량 업데이트에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	//체크박스 업데이트
	@PutMapping("/update/selected")
	public ResponseEntity<?> updateSelected(
			@RequestParam("cartIds") List<Integer> cartIds,
	        @RequestParam("selecteds") List<String> selecteds,
	        HttpSession session) {
	    MemberDto user = (MemberDto) session.getAttribute("dto");

	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }

	    //명시적 검증
	    if (cartIds.size() != selecteds.size()) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", false);
	        response.put("message", "잘못된 입력입니다.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }

	    int result = cartbiz.update(user.getUserId(), cartIds, selecteds);

	    Map<String, Object> response = new HashMap<>();
	    if (result > 0) {
	        response.put("success", true);
	        response.put("message", "체크 박스 업데이트가 성공적으로 처리되었습니다.");
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("success", false);
	        response.put("message", "체크 박스 업데이트에 실패했습니다.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}
	
	// 삭제
	@PutMapping("/delete")
	public ResponseEntity<?> delete(@RequestParam List<Integer> cartIds, HttpSession session) {
		MemberDto user = (MemberDto) session.getAttribute("dto");

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}

		int result = cartbiz.delete(cartIds, user.getUserId());

		Map<String, String> response = new HashMap<>();
		if (result > 0) {
			response.put("message", "선택된 항목이 성공적으로 삭제되었습니다.");
			return ResponseEntity.ok(response);
		} else {
			response.put("message", "선택된 항목 삭제를 실패하였습니다.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
