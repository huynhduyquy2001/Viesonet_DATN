package com.viesonet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Products;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.entity.Users;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, Integer> {
    @Query("SELECT obj FROM ShoppingCart obj WHERE obj.product.productId =:productId AND obj.user.userId=:userId AND obj.color=:color")

    public ShoppingCart findCartByProductId(String userId, int productId, String color);

}
