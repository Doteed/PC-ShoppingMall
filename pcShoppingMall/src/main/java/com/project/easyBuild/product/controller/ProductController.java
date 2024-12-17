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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.easyBuild.product.biz.ProductBiz;
import com.project.easyBuild.product.dto.ProductDto;

@Controller
@RequestMapping("/api/product")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductBiz productBiz;

    @Autowired
    public ProductController(ProductBiz productBiz) {
        this.productBiz = productBiz;
    }

    @GetMapping("/productDto")
    public ResponseEntity<List<ProductDto>> getRest() {
        logger.info("Fetching all products");
        return ResponseEntity.ok(productBiz.listAll());
    }

    @PostMapping("/updateStockStatus")
    public ResponseEntity<Map<String, Boolean>> updateStockStatus(@RequestBody Map<String, Object> payload) {
        Integer productId = (Integer) payload.get("productId");
        Boolean outOfStock = (Boolean) payload.get("outOfStock");
        
        if (productId == null || outOfStock == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
        }
        
        int updatedRows = productBiz.updateStockStatus(productId, outOfStock ? 0 : 1);
        return ResponseEntity.ok(Collections.singletonMap("success", updatedRows > 0));
    }

    @PostMapping("/auth-product-insert")
    public String authProductInsert(@ModelAttribute ProductDto dto, RedirectAttributes redirectAttributes) {
        logger.info("Inserting new product: {}", dto);
        int res = productBiz.insert(dto);

        if (res > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Product inserted successfully.");
            return "redirect:/auth-product";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to insert product.");
            return "redirect:/auth-product-insert";
        }
    }
}
