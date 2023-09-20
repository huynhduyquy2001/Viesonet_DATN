package com.viesonet.dao;

import com.viesonet.entity.Posts;
import com.viesonet.entity.Products;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductsDao extends JpaRepository<Products, Integer> {
    @Query("SELECT p FROM Products p WHERE p.user.userId IN :userId AND p.productStatus.statusId=1")
    List<Products> getShopping(List<String> userId);

    @Query("SELECT p FROM Products p WHERE p.user.userId IN :userId AND p.productStatus.statusId=1")
    Page<Products> getShoppingByPage(List<String> userId, Pageable pageable);

    @Query("SELECT p FROM Products p WHERE p.productId IN :productId AND p.productStatus.statusId = 1")
    Page<Products> getTrending(List<Integer> productId, Pageable pageable);

    @Query("SELECT p FROM Products p WHERE p.productStatus.statusId = 3")
    Page<Object> findPostsProductWithProcessing(Pageable pageable);

}
