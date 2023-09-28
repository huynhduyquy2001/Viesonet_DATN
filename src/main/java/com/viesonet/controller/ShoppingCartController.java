package com.viesonet.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Products;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.service.ProductsService;
import com.viesonet.service.ShoppingCartService;

@RestController
@CrossOrigin("*")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    ProductsService productsService;

    @GetMapping("/get-product-shoppingcart")
    public List<ShoppingCart> getProductByShoppingCart() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return shoppingCartService.findShoppingCartByUserId(userId);
    }
}
