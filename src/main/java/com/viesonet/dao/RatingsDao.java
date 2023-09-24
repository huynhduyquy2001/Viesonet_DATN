package com.viesonet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Products;
import com.viesonet.entity.Ratings;

public interface RatingsDao extends JpaRepository<Ratings, Integer> {
    @Query("SELECT AVG(p.ratingValue) FROM Ratings p WHERE p.product.productId = :productId")
    Double getAverageRating(int productId);
}
