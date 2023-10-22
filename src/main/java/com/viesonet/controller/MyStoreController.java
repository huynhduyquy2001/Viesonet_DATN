package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Products;
import com.viesonet.service.OrderDetailsService;
import com.viesonet.service.OrdersService;
import com.viesonet.service.ProductsService;

@RestController
@CrossOrigin("*")
public class MyStoreController {
    @Autowired
    ProductsService productsService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailsService orderDetailsService;

    @GetMapping("/mystore/{userId}/{page}")
    public Page<Products> getShoppingByPage(@PathVariable int page, @PathVariable String userId) {
        return productsService.findPostsProductMyStore(page, 9, userId);
    }

    @GetMapping("/mystore-pending/{userId}/{page}")
    public Page<Products> getShoppingByPagePending(@PathVariable int page, @PathVariable String userId) {
        return productsService.findPostsProductPending(page, 9, userId);
    }

    @GetMapping("/searchProductMyStore/{userId}/{search}")
    public Page<Products> searchProductMyStore(@PathVariable String search, @PathVariable String userId) {
        return productsService.findSearchProductMyStore(search, userId, 0, 9);
    }

    @PostMapping("/hideProductMyStore/{userId}/{productId}/{page}")
    public Page<Products> searchProductMyStore(@PathVariable int productId, @PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            productsService.hideProductMyStore(productId);
        } catch (Exception e) {

        }
        return productsService.findPostsProductMyStore(page, 9, userId);
    }
}
