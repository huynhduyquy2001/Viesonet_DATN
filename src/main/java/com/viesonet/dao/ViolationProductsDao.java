package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.ViolationProducts;

public interface ViolationProductsDao extends JpaRepository<ViolationProducts, Integer> {

    @Query("SELECT v FROM ViolationProducts v WHERE v.product.productId = :productId and v.user.userId = :userId")
    ViolationProducts findViolationProductsById(int productId, String userId);

}
