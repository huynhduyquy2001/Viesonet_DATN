package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Products;
import com.viesonet.entity.Users;
import com.viesonet.service.FavoriteProductService;

@RestController
@CrossOrigin("*")
public class FavoriteProductsController {
    @Autowired
    FavoriteProductService favoriteProductService;

    @GetMapping("/get-favoriteProducts")
    // public ResponseEntity<List<Products>> getFavoriteProductsByUserId() {
    // // Lấy userId từ SecurityContextHolder
    // String userId =
    // SecurityContextHolder.getContext().getAuthentication().getName();
    // System.out.println(userId + "USERIDDĐ");
    // List<Products> favoriteProducts =
    // favoriteProductService.findFavoriteProductsByUserId(userId);
    // return ResponseEntity.ok(favoriteProducts);
    // }

    public List<Products> getFollowingInfoByUserId1() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return favoriteProductService.findFavoriteProductsByUserId(userId);
    }
}
