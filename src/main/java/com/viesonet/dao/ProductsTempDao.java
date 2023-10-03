package com.viesonet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.ProductsTemp;

public interface ProductsTempDao extends JpaRepository<ProductsTemp, Integer> {
    
    @Query("SELECT p FROM ProductsTemp p")
    Page<Object> findProduct(Pageable pageable);
}

