package com.viesonet.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Comments;
import com.viesonet.entity.FavoriteProducts;
import com.viesonet.entity.Favorites;

public interface FavoriteProductsDao extends JpaRepository<FavoriteProducts, Integer> {
    @Query("SELECT obj from FavoriteProducts obj where obj.user.userId=?1 and obj.product.productId=?2")
    FavoriteProducts findFavoriteProduct(String userId, int productId);
}
