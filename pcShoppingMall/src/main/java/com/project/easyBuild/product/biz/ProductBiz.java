package com.project.easyBuild.product.biz;

import java.util.List;

import com.project.easyBuild.product.dto.ProductDto;

public interface ProductBiz {

	public List<ProductDto> listAll();
	
	public int updateStockStatus(int productId, int stock);
	
	public void decreaseStock(int productId, int quantity);
	
	public int insert(ProductDto dto);
}