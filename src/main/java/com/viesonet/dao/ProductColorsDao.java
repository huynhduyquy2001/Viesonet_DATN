package com.viesonet.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Colors;
import com.viesonet.entity.Posts;
import com.viesonet.entity.ProductColors;

public interface ProductColorsDao extends JpaRepository<ProductColors, Integer> {

    @Query("SELECT p FROM ProductColors p WHERE p.color.colorId = :colorId AND p.product.productId = :productId")
    ProductColors findProductColor(int colorId, int productId);

}
