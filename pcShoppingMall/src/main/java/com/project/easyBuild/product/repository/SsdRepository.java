package com.project.easyBuild.product.repository;

import com.project.easyBuild.product.model.ssd;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SsdRepository extends JpaRepository<ssd, Long>{
	// 기본 CRUD는 JpaRepository에서 제공
	// 제조사를 기준으로 필터링
	List<ssd> findByManufacturerIn(List<String> manufacturers);

}
