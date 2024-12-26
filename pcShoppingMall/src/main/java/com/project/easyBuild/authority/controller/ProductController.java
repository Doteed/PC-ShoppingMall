package com.project.easyBuild.authority.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

import com.project.easyBuild.authority.biz.GitHubService;
import com.project.easyBuild.authority.biz.ProductBiz;
import com.project.easyBuild.authority.dto.ProductDto;

@Controller
@RequestMapping("/api/product")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductBiz productBiz;
    private final GitHubService gitHubService;
        
    @Autowired
    public ProductController(ProductBiz productBiz, GitHubService gitHubService) {
        this.productBiz = productBiz;
        this.gitHubService = gitHubService;
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
    public String authProductInsert(@ModelAttribute ProductDto dto, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            String repoName = "Doteed/PC-ShoppingMall";
            String path = "pcShoppingMall/src/main/resources/static/images/products";  // 끝에 슬래시 제거
            String imageUrl = gitHubService.uploadImage(repoName, path, file);
            dto.setImageUrl(imageUrl);
            
            int res = productBiz.insert(dto);
            if (res > 0) {
                redirectAttributes.addFlashAttribute("successMessage", "Product inserted successfully.");
                return "redirect:/auth-product";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to insert product.");
                return "redirect:/auth-product-insert";
            }
        } catch (IOException e) {
            logger.error("Image upload error", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload image.");
            return "redirect:/auth-product-insert";
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadProductImage(@RequestParam("file") MultipartFile file, @RequestParam("productId") Integer productId) {
        try {
        	String repoName = "Doteed/PC-ShoppingMall";
        	String path = "pcShoppingMall/src/main/resources/static/images/products/" + file.getOriginalFilename();
            String imageUrl = gitHubService.uploadImage(repoName, path, file);

            // 새 이미지 URL로 제품 정보 업데이트
            int updatedRows = productBiz.updateProductImage(productId, imageUrl);

            if (updatedRows > 0) {
                Map<String, String> response = new HashMap<>();
                response.put("imageUrl", imageUrl);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "제품 이미지 업데이트 실패"));
            }
        } catch (IOException e) {
            logger.error("이미지 업로드 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "이미지 업로드 실패"));
        }
    }

    @GetMapping("/product-image/{productId}")
    public ResponseEntity<Map<String, String>> getProductImage(@PathVariable Integer productId) {
        String imageUrl = productBiz.getProductImageUrl(productId);
        if (imageUrl != null) {
            return ResponseEntity.ok(Collections.singletonMap("imageUrl", imageUrl));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    


}
