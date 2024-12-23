package com.project.easyBuild.authority.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.easyBuild.authority.dao.ProductDao;
import com.project.easyBuild.authority.dto.ProductDto;

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
    
    @Override
    public Page<ProductDto> listAllPaginated(Pageable pageable) {
        return dao.listAllPaginated(pageable);
    }


}
