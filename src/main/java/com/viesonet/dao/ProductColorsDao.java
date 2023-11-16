package com.viesonet.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Colors;
import com.viesonet.entity.ProductColors;

public interface ProductColorsDao extends JpaRepository<ProductColors, Integer> {
    @Query("SELECT p FROM ProductColors p WHERE p.product.productId =:productId AND p.color.colorName =:colorName")
    ProductColors findByProductIdAndColor(int productId, String colorName);
}
