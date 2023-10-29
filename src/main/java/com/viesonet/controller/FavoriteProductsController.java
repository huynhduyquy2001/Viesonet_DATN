package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.dao.ProductsDao;
import com.viesonet.entity.FavoriteProducts;
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;
import com.viesonet.service.FavoriteProductService;
import com.viesonet.service.ProductsService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin("*")
public class FavoriteProductsController {
    private ProductsDao productsDao;
    @Autowired
    FavoriteProductService favoriteProductService;

    @Autowired
    UsersService usersService;

    @Autowired
    ProductsService productsService;

    @GetMapping("/get-favoriteProducts/{page}")
    public Page<Products> getFollowingInfoByUserId1(@PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return favoriteProductService.findFavoriteProductsByUserId(userId, page, 8);
    }

    @PostMapping("/addfavoriteproduct/{productId}")
    public FavoriteProducts addFavoriteProduct(@PathVariable String productId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return favoriteProductService.addFavoriteProduct(usersService.findUserById(userId),
                productsService.getProduct(Integer.parseInt(productId)));
    }

    @GetMapping("/find-productFavorite-by-name/{page}")
    public Page<Products> findProductFavoriteByName(@PathVariable int page, @RequestParam String keyF) {
        return productsService.findProductFavoritByName(page, keyF);
    }

}
