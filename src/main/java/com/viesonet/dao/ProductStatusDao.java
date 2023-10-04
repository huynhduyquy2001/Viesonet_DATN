package com.viesonet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viesonet.entity.ProductStatus;
import com.viesonet.entity.Products;

public interface ProductStatusDao extends JpaRepository<ProductStatus, Integer> {

}
