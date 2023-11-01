package com.viesonet.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.ViolationProducts;
import com.viesonet.entity.ViolationsProduct;

public interface ViolationsProductDao extends JpaRepository<ViolationsProduct, Integer> {

    @Query("SELECT v FROM ViolationsProduct v WHERE v.product.productId = :productId and v.userId = :userId")
    ViolationsProduct findViolationProductsById(int productId, String userId);

    @Query("SELECT DISTINCT v FROM ViolationsProduct v WHERE v.status= false")
    Page<Object> findProductWithFalse(Pageable pageable);

    @Query("SELECT DISTINCT v FROM ViolationsProduct v WHERE v.status= true")
    Page<Object> findProductWithTrue(Pageable pageable);

    @Query("SELECT COUNT(v) FROM ViolationsProduct v WHERE v.product.productId = :productId")
    Integer countViolations(int productId);

    @Query("SELECT v.description FROM ViolationsProduct v WHERE v.product.productId = :productId")
    List<String> getDescriptionByProductId(int productId);

    @Query("SELECT v FROM ViolationsProduct v WHERE v.product.productId = :productId")
    List<ViolationsProduct> findByProductId(int productId);

    @Query("SELECT v.product.productId FROM ViolationsProduct v WHERE v.violationId = :violationId")
    Integer getProductId(int violationId);

    @Query("SELECT v FROM ViolationsProduct v WHERE v.status= false and v.product.productName LIKE %:name%")
    List<Object> findSearchProducts(@Param("name") String name);

    @Query("SELECT v FROM ViolationsProduct v WHERE v.status= true and v.product.productName LIKE %:name%")
    List<Object> findSearchViolationProducts(@Param("name") String name);
}
