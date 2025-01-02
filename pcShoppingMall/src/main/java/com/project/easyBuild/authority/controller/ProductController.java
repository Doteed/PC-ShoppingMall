package com.project.easyBuild.authority.controller;

import java.util.Arrays;
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
    
    @GetMapping("/list")
    public ResponseEntity<List<ProductDto>> getProductList() {
        logger.info("Fetching all products");
        List<ProductDto> products = productBiz.listAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/productDto")
    public ResponseEntity<List<ProductDto>> getRest() {
        logger.info("Fetching all products");
        return ResponseEntity.ok(productBiz.listAll());
    }

    @PostMapping("/updateStockStatus")
    public ResponseEntity<Map<String, Object>> updateStockStatus(@RequestBody Map<String, Object> payload) {
        Integer productId = (Integer) payload.get("productId");
        String pSoldout = (String) payload.get("pSoldout");
        if (productId == null || pSoldout == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
        }
        
        logger.info("Updating stock status for product id: {}", productId);
        int updatedRows = productBiz.updateSoldOutStatus(productId, pSoldout);
        return ResponseEntity.ok(Collections.singletonMap("success", updatedRows > 0));
    }

    @PostMapping("/auth-product-insert")
    public String authProductInsert(@ModelAttribute ProductDto dto, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Inserting new product: {}", dto.getpName());
            String repoName = "Doteed/PC-ShoppingMall";
            String path = "pcShoppingMall/src/main/resources/static/images/products";
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
    public ResponseEntity<Map<String, String>> uploadProductImage(
        @RequestParam("file") MultipartFile file, 
        @RequestParam("productId") Integer productId) {

        if (file.isEmpty() || productId == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid input data"));
        }

        try {
            String imageUrl = gitHubService.uploadImage(
                "Doteed/PC-ShoppingMall", 
                "pcShoppingMall/src/main/resources/static/images/products", 
                file
            );

            int updatedRows = productBiz.updateProductImage(productId, imageUrl);

            if (updatedRows > 0) {
                return ResponseEntity.ok(Collections.singletonMap("imageUrl", imageUrl));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Image update failed"));
            }
        } catch (IOException e) {
            logger.error("Image upload error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Image upload failed"));
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
    
    @PostMapping("/update-stock")
    public ResponseEntity<Map<String, Boolean>> updateStock(@RequestBody Map<String, Object> payload) {
        Integer productId = (Integer) payload.get("productId");
        Integer quantity = (Integer) payload.get("quantity");
        
        if (productId == null || quantity == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
        }
        
        int updatedRows = productBiz.updateStock(productId, quantity);
        boolean success = updatedRows > 0;
        return ResponseEntity.ok(Collections.singletonMap("success", success));
    }

    @PostMapping("/update-sale-status")
    public ResponseEntity<Map<String, Boolean>> updateSaleStatus(@RequestBody Map<String, Object> payload) {
        Integer productId = (Integer) payload.get("productId");
        String status = (String) payload.get("status");
        
        if (productId == null || status == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
        }
        
        int updatedRows = productBiz.updateSaleStatus(productId, status);
        boolean success = updatedRows > 0;
        return ResponseEntity.ok(Collections.singletonMap("success", success));
    }

    @PostMapping("/update-soldout-status")
    public ResponseEntity<Map<String, Boolean>> updateSoldOutStatus(@RequestBody Map<String, Object> payload) {
        Integer productId = (Integer) payload.get("productId");
        String status = (String) payload.get("status");
        
        if (productId == null || status == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("success", false));
        }
        
        int updatedRows = productBiz.updateSoldOutStatus(productId, status);
        boolean success = updatedRows > 0;
        return ResponseEntity.ok(Collections.singletonMap("success", success));
    }
    
    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateProduct(@RequestBody Map<String, Object> payload) {
        try {
            Integer productId = Integer.valueOf(String.valueOf(payload.get("productId")));
            Integer stock = Integer.valueOf(String.valueOf(payload.get("stock")));
            Integer pReportstock = Integer.valueOf(String.valueOf(payload.get("pReportstock")));
            String saleStatus = (String) payload.get("saleStatus");
            String pSoldout = (String) payload.get("pSoldout");

            if (productId == null || stock == null || pReportstock == null || saleStatus == null || pSoldout == null ||
                stock < 0 || pReportstock < 0 || !Arrays.asList("Y", "N").contains(saleStatus) || !Arrays.asList("Y", "N").contains(pSoldout)) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Invalid input data"));
            }

            logger.info("Updating product: id={}, stock={}, pReportstock={}, saleStatus={}, outOfStock={}", 
                        productId, stock, pReportstock, saleStatus, pSoldout);

            boolean success = productBiz.updateProduct(productId, stock, pReportstock, saleStatus, pSoldout);
            return ResponseEntity.ok(Map.of("success", success, "message", "상품 정보가 성공적으로 업데이트되었습니다."));
        } catch (Exception e) {
            logger.error("Error updating product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }



    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable int productId) {
        ProductDto product = productBiz.getProductById(productId); // Biz 계층에서 호출

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

}
