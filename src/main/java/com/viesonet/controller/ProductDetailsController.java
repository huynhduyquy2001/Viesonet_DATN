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

import com.viesonet.entity.Products;
import com.viesonet.entity.Ratings;
import com.viesonet.entity.Users;
import com.viesonet.service.ProductsService;
import com.viesonet.service.RatingsService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin("*")
public class ProductDetailsController {
    @Autowired
    ProductsService productsService;

    @Autowired
    RatingsService ratingsService;

    @Autowired
    UsersService usersService;

    @GetMapping("/get-product/{productId}")
    public Products getProduct(@PathVariable int productId) {
        return productsService.getProduct(productId);
    }

    @PostMapping("/rate-product/{productId}")
    public ResponseEntity<Ratings> rateProduct(@RequestBody Ratings ratingRequest, @PathVariable int productId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ratingRequest.setUser(usersService.getUserById(userId));
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

    @GetMapping("/get-related-products/{userId}")
    public List<Products> getRelatedProducts(@PathVariable String userId) {
        return productsService.getRelatedProducts(userId);
    }

}
