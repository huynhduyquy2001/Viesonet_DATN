package com.viesonet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viesonet.entity.RejectProducts;

@Repository
public interface RejectProductsDao extends JpaRepository<RejectProducts, Integer> {
    @Query("SELECT p FROM RejectProducts p")
    Page<Object> getProducts(Pageable pageable);
}

