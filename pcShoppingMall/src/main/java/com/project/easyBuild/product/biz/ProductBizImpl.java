package com.project.easyBuild.product.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.easyBuild.product.dao.ProductDao;
import com.project.easyBuild.product.dto.ProductDto;

@Service
public class ProductBizImpl implements ProductBiz {
	@Autowired
	@Qualifier("productDaoImple")
	private ProductDao dao;
	
	public List<ProductDto> listAll() {
		return dao.listAll();
	}

	@Override
	@Transactional
	public int updateStockStatus(int productId, int stock) {
	    return dao.updateStockStatus(productId, stock);
	}
	
	@Override
	public void decreaseStock(int productId, int quantity) {
	}

	@Override
	public int insert(ProductDto dto) {
		return dao.insert(dto);
	}

    @Override
    public int updateProductImage(Integer productId, String imageUrl) {
        return dao.updateProductImage(productId, imageUrl);
    }

    @Override
    public String getProductImageUrl(Integer productId) {
        return dao.getProductImageUrl(productId);
    }

}
