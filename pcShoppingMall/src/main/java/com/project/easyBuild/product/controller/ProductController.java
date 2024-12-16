package com.project.easyBuild.product.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.easyBuild.product.biz.ProductBiz;
import com.project.easyBuild.product.dto.ProductDto;

@RestController
public class ProductController {
	
	private Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductBiz productbiz;
	
	@GetMapping("/productDto")
	public ResponseEntity<List<ProductDto>> getRest() {
	    logger.info("list");
	    
	    List<ProductDto> list = productbiz.listAll();
	    
	    return ResponseEntity.ok(list);
	}
	
	@PostMapping("/api/product/updateStockStatus")
	public ResponseEntity<Map<String, Boolean>> updateStockStatus(@RequestBody Map<String, Object> payload) {
		Integer productId = (Integer) payload.get("productId");
	    Boolean outOfStock = (Boolean) payload.get("outOfStock");
	    
	    if (productId == null || outOfStock == null) {
	        return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
	    }
	    
	    int updatedRows = productbiz.updateStockStatus(productId, outOfStock ? 0 : 1);
	    
	    Map<String, Boolean> response = new HashMap<>();
	    response.put("success", updatedRows > 0);
	    
	    return ResponseEntity.ok(response);
	}
	
}
