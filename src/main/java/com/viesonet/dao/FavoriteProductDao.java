package com.viesonet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.viesonet.entity.FavoriteProducts;

public interface FavoriteProductDao extends JpaRepository<FavoriteProducts, String> {
    @Query("SELECT f FROM FavoriteProducts f WHERE f.user.userId =:userId and f.product.productId =:productId")
    FavoriteProducts findByUserIdAndProductId(String userId, int productId);
}
