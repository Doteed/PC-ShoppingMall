package com.project.easyBuild.product.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.easyBuild.product.dao.ProductDao;
import com.project.easyBuild.product.dto.ProductDto;

@Service
public class ProductBizImpl implements ProductBiz {
	@Autowired
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
	    // TODO: 해당 메서드 구현 필요
	}

}
