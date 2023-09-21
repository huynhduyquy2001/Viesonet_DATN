package com.viesonet.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viesonet.entity.ShoppingCart;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, String> {

}
