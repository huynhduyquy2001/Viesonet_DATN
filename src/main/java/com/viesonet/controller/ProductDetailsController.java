package com.viesonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Products;
import com.viesonet.service.ProductsService;

@RestController
@CrossOrigin("*")
public class ProductDetailsController {
    @Autowired
    ProductsService productsService;

    @GetMapping("/get-product/{productId}")
    public Products getProduct(@PathVariable int productId) {
        return productsService.getProduct(productId);
    }
}
