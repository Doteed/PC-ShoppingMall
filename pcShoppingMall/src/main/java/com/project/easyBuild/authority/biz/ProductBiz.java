package com.project.easyBuild.authority.biz;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.easyBuild.authority.dto.ProductDto;

public interface ProductBiz {

	public List<ProductDto> listAll();
	
	public int updateStockStatus(int productId, int stock);
	
	public void decreaseStock(int productId, int quantity);
	
	public int insert(ProductDto dto);
	
	public int updateProductImage(Integer productId, String imageUrl);
	
	public String getProductImageUrl(Integer productId);
	
	public Page<ProductDto> listAllPaginated(Pageable pageable);
		
	public int updateStock(int productId, int quantity);
	
	public int updateSaleStatus(int productId, String status);
	
	public int updateSoldOutStatus(int productId, String status);
	
	public boolean updateProduct(int productId, int stock, int pReportstock, String saleStatus, String pSoldout);
	
	public ProductDto getProductById(int productId);
	
	public int insertWithCategories(ProductDto dto, List<Integer> categoryIds);
	
	public Page<ProductDto> listByCategory1(Long categoryId, Pageable pageable);
	public Page<ProductDto> listByCategory2(Long categoryId, Pageable pageable);
	public Page<ProductDto> listByCategory3(Long categoryId, Pageable pageable);
	
	public boolean deleteProduct(int productId)

}