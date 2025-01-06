package com.project.easyBuild.authority.controller;

import com.project.easyBuild.authority.biz.CategoryBiz;
import com.project.easyBuild.authority.dto.CategoryDto;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private final CategoryBiz categoryBiz;

    @Autowired
    public CategoryController(CategoryBiz categoryBiz) {
        this.categoryBiz = categoryBiz;
    }

    @GetMapping("/manage")
    public String manageCategoriesPage(Model model) {
        model.addAttribute("categories", categoryBiz.listAll());
        return "pages/authority/auth-category";
    }
    
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<?> getAllCategories() {
        try {
            List<CategoryDto> categories = categoryBiz.listAll();
            // 로그 추가
            System.out.println("Retrieved categories: " + categories);
            // 클라이언트가 기대하는 형식으로 데이터 변환
            Map<Integer, List<CategoryDto>> categoriesByLevel = categories.stream()
                .collect(Collectors.groupingBy(CategoryDto::getCategoryLevel));
            return ResponseEntity.ok(categoriesByLevel);
        } catch (Exception e) {
            // 로그 추가
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addCategories(@RequestBody List<CategoryDto> categories) {
        List<CategoryDto> toInsert = new ArrayList<>();
        List<CategoryDto> toUpdate = new ArrayList<>();

        for (CategoryDto category : categories) {
            if (category.getCategoryName() == null || category.getCategoryCode() == null || category.getCategoryLevel() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "유효하지 않은 카테고리 데이터"));
            }
            // parentId가 0인 경우 null로 설정
            if (category.getParentId() != null && category.getParentId() == 0) {
                category.setParentId(null);
            }
            if (categoryBiz.existsByCategoryCode(category.getCategoryCode())) {
                toUpdate.add(category);
            } else {
                toInsert.add(category);
            }
        }

        try {
            int insertedCount = 0;
            int updatedCount = 0;

            if (!toInsert.isEmpty()) {
                categoryBiz.insertBatch(toInsert);
                insertedCount = toInsert.size();
            }

            if (!toUpdate.isEmpty()) {
                for (CategoryDto category : toUpdate) {
                    categoryBiz.update(category);
                    updatedCount++;
                }
            }

            String message = String.format("%d개의 카테고리가 추가되고, %d개의 카테고리가 업데이트되었습니다.", insertedCount, updatedCount);
            return ResponseEntity.ok().body(Map.of("message", message));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "카테고리 처리 중 오류가 발생했습니다"));
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDto categoryDto) {
        try {
            categoryBiz.update(categoryDto);
            return ResponseEntity.ok().body(Map.of("message", "카테고리가 성공적으로 업데이트되었습니다"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "카테고리 업데이트 중 오류가 발생했습니다"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryBiz.delete(id);
            return ResponseEntity.ok().body(Map.of("message", "Category deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @ControllerAdvice
    public class GlobalExceptionHandler {
        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleAllExceptions(Exception ex) {
            logger.error("Unexpected error", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
            logger.error("JSON parse error", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format: " + ex.getMessage());
        }
    }
    
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(required = false) Long parentId,
            @RequestParam int level) {
        try {
            List<CategoryDto> categories = categoryBiz.getCategoriesByLevel(level, parentId);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/subcategories")
    @ResponseBody
    public ResponseEntity<Map<Integer, List<CategoryDto>>> getSubCategories(@RequestParam Long parentId) {
        try {
            List<CategoryDto> secondLevel = categoryBiz.getCategoriesByLevel(2, parentId);
            List<CategoryDto> thirdLevel = new ArrayList<>();
            for (CategoryDto secondCategory : secondLevel) {
                thirdLevel.addAll(categoryBiz.getCategoriesByLevel(3, secondCategory.getCategoryId()));
            }
            
            Map<Integer, List<CategoryDto>> result = new HashMap<>();
            result.put(2, secondLevel);
            result.put(3, thirdLevel);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
