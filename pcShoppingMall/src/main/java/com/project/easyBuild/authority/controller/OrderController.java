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

import java.util.List;

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
    public String listOne(@PathVariable int orderId,  String userId, Model model) {
        OrderDto order = orderBiz.listOne(orderId, userId);
        model.addAttribute("orderDto", order);
        return "order/detail";
    }

    @PostMapping("/update")
    public String updateOrder(@ModelAttribute("orderDto") OrderDto orderDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<OrderDto> orders = orderBiz.listAll();
            model.addAttribute("orders", orders);
            return "pages/authority/auth-order";
        }
        orderBiz.myUpdate(orderDto);
        return "redirect:/order/list";
    }


    @PostMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable int orderId, String userId) {
        orderBiz.cancle(orderId, userId);
        return "redirect:/order/list";
    }
}
