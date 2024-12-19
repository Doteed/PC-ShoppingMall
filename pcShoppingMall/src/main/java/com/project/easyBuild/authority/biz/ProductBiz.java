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

}