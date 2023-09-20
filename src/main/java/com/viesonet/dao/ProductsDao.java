package com.viesonet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viesonet.entity.Products;

public interface ProductsDao extends JpaRepository<Products, Integer> {

}
