package com.viesonet.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.ProductsTemp;

public interface ProductsTempDao extends JpaRepository<ProductsTemp, Integer> {

    @Query("SELECT p FROM ProductsTemp p")
    Page<Object> findProduct(Pageable pageable);

    @Query("SELECT p FROM ProductsTemp p WHERE p.productId = :productId")
    Optional<Object> findByProductId(@Param("productId") int productId);

}
