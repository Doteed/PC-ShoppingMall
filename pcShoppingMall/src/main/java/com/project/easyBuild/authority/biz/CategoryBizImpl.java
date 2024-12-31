package com.project.easyBuild.authority.biz;

import com.project.easyBuild.authority.dao.CategoryDao;
import com.project.easyBuild.authority.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CategoryBizImpl implements CategoryBiz {
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryBizImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<CategoryDto> listAll() {
        return categoryDao.listAll();
    }

    @Override
    public boolean insert(CategoryDto dto) {
        return categoryDao.insert(dto) > 0;
    }

    @Override
    public boolean update(CategoryDto dto) {
        return categoryDao.update(dto) > 0;
    }

    @Override
    public boolean delete(Long id) {
        return categoryDao.delete(id) > 0;
    }
    
    @Transactional
    public void insertBatch(List<CategoryDto> categories) {
        try {
            categoryDao.insertBatch(categories);
        } catch (Exception e) {
            throw new RuntimeException("카테고리 삽입 중 오류 발생: " + e.getMessage(), e);
        }
    }
    
}
