package com.project.easyBuild.authority.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.project.easyBuild.entire.biz.OrderBiz;
import com.project.easyBuild.entire.dto.OrderDto;
import com.project.easyBuild.member.dto.MemberDto;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderBiz orderBiz;

    @GetMapping("/list")
    public String listOrders(Model model) {
        logger.info("listOrders 메서드 호출됨");
        List<OrderDto> orders = orderBiz.listAll();
        model.addAttribute("orders", orders);
        model.addAttribute("orderDto", new OrderDto());
        return "pages/authority/auth-order";
    }

    @GetMapping("/{orderId}")
    @ResponseBody
    public ResponseEntity<?> getOrderDetails(@PathVariable int orderId, HttpSession session) {
        MemberDto user = (MemberDto) session.getAttribute("dto");
        if (user == null) {
            // 로그인하지 않은 경우 JSON 형태로 응답
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("error", "Unauthorized", "redirectUrl", "/loginform"));
        }

        try {
            OrderDto order = orderBiz.listOne(orderId, user.getUserId());
            if (order != null) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(order);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", "Not Found", "message", "주문을 찾을 수 없습니다."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateOrder(@RequestBody OrderDto orderDto, HttpSession session) {
        MemberDto user = (MemberDto) session.getAttribute("dto");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        int result = orderBiz.updateOrder(orderDto, user.getUserId());
        if (result > 0) {
            return ResponseEntity.ok("주문이 성공적으로 업데이트되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("주문 업데이트에 실패했습니다.");
        }
    }

    @PostMapping("/cancel/{orderId}")
    @ResponseBody
    public ResponseEntity<?> cancelOrder(@PathVariable int orderId, HttpSession session) {
    	MemberDto user = (MemberDto) session.getAttribute("dto");
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("redirectUrl", "/loginform"));
		}
		
    	int result = orderBiz.cancle(orderId, user.getUserId(), user.getAuthId());
        if (result > 0) {
            return ResponseEntity.ok("주문이 성공적으로 취소되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("주문 취소에 실패했습니다.");
        }
    }
    
    @GetMapping("/monthly-sales/{year}")
    @ResponseBody
    public ResponseEntity<List<OrderDto>> getMonthlySales(@PathVariable int year) {
        List<OrderDto> monthlySales = orderBiz.getMonthlySales(year);
        return ResponseEntity.ok(monthlySales);
    }
}
