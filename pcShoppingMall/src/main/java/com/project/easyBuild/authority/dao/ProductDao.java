package com.project.easyBuild.authority.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.easyBuild.authority.dto.ProductDto;

public interface ProductDao {
	String NAMESPACE = "product.";
	
	public List<ProductDto> listAll();

	public int updateStockStatus(int productId, int stock);
	
	public int insert(ProductDto dto);
	
	public int updateProductImage(Integer productId, String imageUrl);
	
	public String getProductImageUrl(Integer productId);
	
	public Page<ProductDto> listAllPaginated(Pageable pageable);
	
	public int updateStock(int productId, int quantity);
	
	public int updateSaleStatus(int productId, String status);
	
	public int updateSoldOutStatus(int productId, String status);

	public int updateProduct(int productId, int stock, int pReportstock, String saleStatus);
	
	public ProductDto getProductById(int productId);

}
