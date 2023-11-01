package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.ShoppingCart;

public interface ShoppingCartDao extends JpaRepository<ShoppingCart, Integer> {
    @Query("SELECT obj FROM ShoppingCart obj WHERE obj.product.productId =:productId AND obj.user.userId=:userId AND obj.color=:color")
    public ShoppingCart findCartByProductId(String userId, int productId, String color);

    @Query("SELECT obj FROM ShoppingCart obj WHERE obj.user.userId = :userId")
    public List<ShoppingCart> findCartByUserId(String userId);

    @Query("SELECT obj FROM ShoppingCart obj WHERE obj.product.productId IN :productId AND obj.user.userId = :userId AND obj.color IN :color")
    public List<ShoppingCart> findListProduct(String userId, List<Integer> productId, List<String> color);

}