package com.project.easyBuild.user.controller;

import java.util.ArrayList;
import java.util.Collections;
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

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartBiz cartbiz;

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

	@GetMapping("/checkCart")
    public ResponseEntity<?> checkCart(HttpSession session) {
		MemberDto user = (MemberDto) session.getAttribute("dto");
		
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
		boolean cartNotEmpty = cartbiz.isCartNotEmpty(user.getUserId());
		System.out.println(cartNotEmpty);
		return ResponseEntity.ok(cartNotEmpty);
    }
	
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
		System.out.println("Product ID: " + productId + ", Quantity: " + quantity);
		
		int result = cartbiz.insert(productId, quantity, user.getUserId());
		
		System.out.println("Insert result: " + result);
		
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
	
	// 삭제
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(@RequestBody Map<String, List<Integer>> requestBody, HttpSession session) {
		MemberDto user = (MemberDto) session.getAttribute("dto");

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}

		List<Integer> cartIds = requestBody.get("cartIds");
		
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
	
	// 전체 삭제
	@DeleteMapping("/deleteAll")
	public ResponseEntity<?> deleteAll(HttpSession session) {
	    MemberDto user = (MemberDto) session.getAttribute("dto");

	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
	    }

	    int result = cartbiz.deleteAll(user.getUserId());

	    Map<String, String> response = new HashMap<>();
	    if (result > 0) {
	        response.put("message", "장바구니의 모든 항목이 성공적으로 삭제되었습니다.");
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("message", "장바구니 항목 전체 삭제를 실패하였습니다.");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	}
}
