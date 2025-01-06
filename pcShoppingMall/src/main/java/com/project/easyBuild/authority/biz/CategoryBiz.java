package com.project.easyBuild.authority.biz;

import com.project.easyBuild.authority.dto.CategoryDto;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface CategoryBiz {
	public List<CategoryDto> listAll();

	public boolean insert(CategoryDto dto);
	
	public boolean update(CategoryDto dto);
	
	public boolean delete(Long id);
	
	public void insertBatch(List<CategoryDto> categories);

	public List<CategoryDto> getCategoriesByParentAndLevel(Long parentId, int level);
	
	public List<CategoryDto> getCategoriesByLevel(int level, Long parentId);
	
	public boolean existsByCategoryCode(String categoryCode);

}
