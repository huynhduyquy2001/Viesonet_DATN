package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.FavoriteProducts;
import com.viesonet.entity.Products;
import com.viesonet.entity.Ratings;
import com.viesonet.entity.Users;
import com.viesonet.service.FavoriteProductService;
import com.viesonet.service.OrdersService;
import com.google.cloud.storage.Acl.User;
import com.viesonet.entity.Colors;
import com.viesonet.entity.Products;
import com.viesonet.entity.Ratings;
import com.viesonet.entity.Users;
import com.viesonet.service.ColorsService;
import com.viesonet.service.ProductsService;
import com.viesonet.service.RatingsService;
import com.viesonet.service.UsersService;
import com.viesonet.service.WordBannedService;

@RestController
@CrossOrigin("*")
public class ProductDetailsController {
    @Autowired
    ProductsService productsService;

    @Autowired
    RatingsService ratingsService;

    @Autowired
    UsersService usersService;

    @Autowired
    FavoriteProductService favoriteProductService;

    @Autowired
    WordBannedService wordBannedService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    ColorsService colorsService;

    @GetMapping("/get-product/{productId}")

    public Products getProduct(@PathVariable int productId) {
        return productsService.getProduct(productId);
    }

    @PostMapping("/rate-product/{productId}")
    public ResponseEntity<Ratings> rateProduct(@RequestBody Ratings ratingRequest, @PathVariable int productId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ratingRequest.setUser(usersService.getUserById(userId));
        ratingRequest.setRatingContent(wordBannedService.wordBanned(ratingRequest.getRatingContent()));
        try {
            Ratings savedRating = ratingsService.rateProduct(ratingRequest, productsService.getProduct(productId));
            if (savedRating != null) {
                return ResponseEntity.ok(savedRating);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/check-bought/{productId}")
    public boolean checkBought(@PathVariable int productId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ordersService.checkBought(userId, productId);
    }

    @GetMapping("/get-related-products/{userId}")
    public List<Products> getRelatedProducts(@PathVariable String userId) {
        return productsService.getRelatedProducts(userId);
    }

    @GetMapping("/get-favorite-product/{productId}")
    public boolean getFavotiteProducts(@PathVariable int productId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return favoriteProductService.getFavoriteProducts(userId, productId);
    }

    @PostMapping("/add-favorite-product/{productId}")
    public FavoriteProducts addFavoriteProduct(@PathVariable int productId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return favoriteProductService.addFavoriteProduct(usersService.findUserById(userId),
                productsService.getProduct(productId));
    }
    // @PostMapping("/api/products/add/{userId}")
    // public ResponseEntity<String> addProduct(@RequestBody Products product,
    // @PathVariable String userId) {
    // try {
    // // Sử dụng userId ở đây nếu cần
    // productsService.addProduct(product, userId);
    // return ResponseEntity.ok("Sản phẩm đã được thêm bởi " + userId);
    // } catch (Exception e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body("Lỗi khi thêm sản phẩm: " + e.getMessage());
    // }
    // }

    @GetMapping
    public List<Products> getAllProducts() {
        return productsService.getAllProducts();
    }

    @PostMapping("/products/add")
    public Products addProduct(@RequestBody Products product) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        productsService.addProduct(product, usersService.findUserById(userId));
        return null;
    }

    @GetMapping("/products/color")
    public List<Colors> getAllColors() {
        return colorsService.getAllColors();
    }

}
