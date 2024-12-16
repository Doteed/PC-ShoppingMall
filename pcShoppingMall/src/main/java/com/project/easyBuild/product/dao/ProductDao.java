package com.project.easyBuild.product.dao;

import java.util.List;

import com.project.easyBuild.product.dto.ProductDto;

public interface ProductDao {
	public List<ProductDto> listAll();

	int updateStockStatus(int productId, int stock);

	void decreaseStock(int productId, int quantity);
}
