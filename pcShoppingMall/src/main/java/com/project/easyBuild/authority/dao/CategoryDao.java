package com.project.easyBuild.authority.dao;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.project.easyBuild.authority.dto.CategoryDto;

public interface CategoryDao {
	public List<CategoryDto> listAll();
    
	public int insert(CategoryDto dto);
	    
	public int update(CategoryDto dto);
    
	public int delete(Long id);
    
	public void insertBatch(List<CategoryDto> categories);
	
	public void insertProductCategory(int productId, Integer categoryId);
	
	public List<CategoryDto> getCategoriesByParentAndLevel(Long parentId, int level);
	
	public List<CategoryDto> getCategoriesByLevel(int level, Long parentId);

	public boolean existsByCategoryCode(String categoryCode);
	
	public boolean existsById(Long id);
}
