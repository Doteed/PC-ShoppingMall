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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.project.easyBuild.authority.biz.CategoryBiz;
import com.project.easyBuild.authority.biz.GitHubService;
import com.project.easyBuild.authority.biz.ProductBiz;
import com.project.easyBuild.authority.dto.CategoryDto;
import com.project.easyBuild.authority.dto.ProductDto;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductBiz productBiz;
    private final GitHubService gitHubService;
    private final ObjectMapper objectMapper;
    private final ProductBiz productbiz;
    private final CategoryBiz categorybiz;
        
    @Autowired
    public ProductController(ProductBiz productBiz, GitHubService gitHubService, ObjectMapper objectMapper, ProductBiz productbiz, CategoryBiz categorybiz) {
        this.productBiz = productBiz;
        this.gitHubService = gitHubService;
        this.objectMapper = objectMapper;
        this.productbiz = productbiz;
        this.categorybiz = categorybiz;
    }

    @GetMapping("/list")
    public String listProducts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) Long category1,
                               @RequestParam(required = false) Long category2,
                               @RequestParam(required = false) Long category3) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductDto> productPage;

        if (category3 != null) {
            productPage = productbiz.listByCategory3(category3, pageable);
        } else if (category2 != null) {
            productPage = productbiz.listByCategory2(category2, pageable);
        } else if (category1 != null) {
            productPage = productbiz.listByCategory1(category1, pageable);
        } else {
            productPage = productbiz.listAllPaginated(pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());

        // 카테고리 데이터 추가
        List<CategoryDto> categories = categorybiz.listAll();
        logger.info("Categories size: {}", categories.size());
        try {
            String categoryDataJson = objectMapper.writeValueAsString(categories);
            model.addAttribute("categoryDataJson", categoryDataJson);
        } catch (JsonProcessingException e) {
            logger.error("Error converting categories to JSON", e);
            model.addAttribute("categoryDataJson", "[]");
        }
        model.addAttribute("category1", category1);
        model.addAttribute("category2", category2);
        model.addAttribute("category3", category3);

        return "pages/authority/auth-product";
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getProductList() {
        logger.info("Fetching all products");
        List<ProductDto> products = productBiz.listAll();
        return ResponseEntity.ok(products);
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

    @PostMapping(value = "/auth-product-insert", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> authProductInsert(
        @ModelAttribute ProductDto dto,
        @RequestParam("file") MultipartFile file,
        @RequestParam("categoryIds") String categoryIdsJson) {
        
        try {
            // JSON 문자열을 List<Integer>로 변환
            List<Integer> categoryIds = objectMapper.readValue(categoryIdsJson, new TypeReference<List<Integer>>(){});
            
            // 카테고리 ID 설정
            if (categoryIds.size() > 0) dto.setCategoryId1(categoryIds.get(0));
            if (categoryIds.size() > 1) dto.setCategoryId2(categoryIds.get(1));
            if (categoryIds.size() > 2) dto.setCategoryId3(categoryIds.get(2));

            // 이미지 업로드 및 URL 설정
            String imageUrl = gitHubService.uploadImage("Doteed/PC-ShoppingMall",
                    "pcShoppingMall/src/main/resources/static/images/products",
                    file);
            dto.setImageUrl(imageUrl);

            // 제품 삽입
            int res = productBiz.insertWithCategories(dto, categoryIds);
            if (res > 0) {
                return ResponseEntity.ok().body(Map.of("message", "Product inserted successfully."));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to insert product."));
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload image or process categories."));
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
    
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable int productId) {
        try {
            boolean deleted = productBiz.deleteProduct(productId);
            if (deleted) {
                return ResponseEntity.ok().body(Map.of("success", true, "message", "상품이 성공적으로 삭제되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "상품을 찾을 수 없습니다."));
            }
        } catch (Exception e) {
            logger.error("Error deleting product", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "상품 삭제 중 오류가 발생했습니다."));
        }
    }


}
