package com.project.easyBuild.authority.dao;

import java.util.List;

import com.project.easyBuild.authority.dto.ProductDto;

public interface ProductDao {
	String NAMESPACE = "product.";
	
	public List<ProductDto> listAll();

	public int updateStockStatus(int productId, int stock);

	public void decreaseStock(int productId, int quantity);
	
	public int insert(ProductDto dto);
	
	public int updateProductImage(Integer productId, String imageUrl);
	public String getProductImageUrl(Integer productId);
}
