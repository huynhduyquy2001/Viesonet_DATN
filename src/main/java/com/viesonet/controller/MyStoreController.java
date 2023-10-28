package com.viesonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Products;
import com.viesonet.service.ProductsService;

@RestController
@CrossOrigin("*")
public class MyStoreController {
    @Autowired
    ProductsService productsService;

    @GetMapping("/get-product-mystore/{page}")
    public Page<Products> getShoppingByPage(@PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return productsService.findPostsProductMyStore(page, 9, userId);
    }
}
