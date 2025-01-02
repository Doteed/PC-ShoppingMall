package com.project.easyBuild.authority.biz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private final Logger logger = LoggerFactory.getLogger(ProductBizImpl.class);
	
	@Autowired
	@Qualifier("productDaoImple")
	private ProductDao dao;
	
	@Override
    @Transactional(readOnly = true)
    public List<ProductDto> listAll() {
        logger.info("Fetching all products");
        return dao.listAll();
    }

	@Override
    @Transactional
    public int updateStockStatus(int productId, int stock) {
        logger.info("Updating stock status for product id: {}", productId);
        return dao.updateStockStatus(productId, stock);
    }

	@Override
    @Transactional
    public int insert(ProductDto dto) {
        logger.info("Inserting new product: {}", dto.getpName());
        return dao.insert(dto);
    }

    @Override
    @Transactional
    public int updateProductImage(Integer productId, String imageUrl) {
        logger.info("Updating product image for product id: {}", productId);
        return dao.updateProductImage(productId, imageUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public String getProductImageUrl(Integer productId) {
        logger.info("Fetching image URL for product id: {}", productId);
        return dao.getProductImageUrl(productId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> listAllPaginated(Pageable pageable) {
        logger.info("Fetching paginated products: page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        return dao.listAllPaginated(pageable);
    }
    
    @Override
    @Transactional
    public int updateStock(int productId, int quantity) {
        logger.info("Updating stock for product id: {} by quantity: {}", productId, quantity);
        return dao.updateStock(productId, quantity);
    }
    
    @Override
    @Transactional
    public int updateSaleStatus(int productId, String status) {
        logger.info("Updating sale status for product id: {} to: {}", productId, status);
        return dao.updateSaleStatus(productId, status);
    }

    @Override
    @Transactional
    public int updateSoldOutStatus(int productId, String pSoldout) {
        logger.info("Updating sold out status for product id: {} to: {}", productId, pSoldout);
        return dao.updateSoldOutStatus(productId, pSoldout);
    }

    @Override
    @Transactional
    public boolean updateProduct(int productId, int stock, int pReportstock, String saleStatus, String pSoldout) {
        logger.info("Updating product id: {}", productId);
        int updatedRows = dao.updateProduct(productId, stock, pReportstock, saleStatus, pSoldout);
        return updatedRows > 0;
    }
        
    @Override
    @Transactional
    public void decreaseStock(int productId, int quantity) {
        ProductDto product = dao.getProductById(productId);

        if (product == null) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다.");
        }

        if (product.getpStock() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        dao.updateStock(productId, -quantity); // 기존 메서드 활용
    }
    
	public ProductDto getProductById(int productId) {
		return dao.getProductById(productId);
	}

}
