package com.project.easyBuild.product.biz;

import java.util.List;

import com.project.easyBuild.product.dto.ProductDto;

public interface ProductBiz {

	public List<ProductDto> listAll();
	
	int updateStockStatus(int productId, int stock);
	
	void decreaseStock(int productId, int quantity);
}