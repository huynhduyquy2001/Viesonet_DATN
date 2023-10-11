package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.viesonet.entity.Notifications;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Products;
import com.viesonet.service.ProductsService;
import com.viesonet.service.ProductsTempService;
import com.viesonet.service.RejectProductsService;

import jakarta.transaction.Transactional;

@CrossOrigin("*")
@RestController
public class PostProductController {

    @Autowired
    ProductsService productsService;

    @Autowired
    ProductsTempService productsTempService;

    @Autowired
    RejectProductsService rejectProductsService;

    // @GetMapping("staff/postsproduct")
    // public ResponseEntity<Page<Object>> postsProduct(@RequestParam(defaultValue =
    // "0") int page,
    // @RequestParam(defaultValue = "9") int size) {
    // Page<Object> result = productsService.findPostsProductWithProcessing(page,
    // size);
    // return ResponseEntity.ok(result);
    // }

    @GetMapping("/staff/postsproduct/{Page}")
    public ResponseEntity<Page<Object>> getProduct(@PathVariable int Page) {
        Page<Object> result = productsTempService.getProducts(Page, 10);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/staff/postsProductDecline/{Page}")
    public ResponseEntity<Page<Object>> getProductDecline(@PathVariable int Page) {
        Page<Object> result = productsService.findPostsProductWithDecline(Page, 10);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/staff/searchProduct")
    public ResponseEntity<List<Object>> searchUserViolation(@RequestParam String name) {
        List<Object> searchResult = productsService.findSearchProducts(name);
        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/staff/postsproduct/detailProduct/{productId}")
    public Products getViolationsByPostId(@PathVariable int productId) {
        return productsService.findProductById(productId);
    }

    @PostMapping("/staff/postsproduct/accept/{productId}")
    public ResponseEntity<Object> accept(@PathVariable int productId) {
        productsService.acceptByProductId(productId);
        Page<Object> result = productsService.findPostsProductWithProcessing(0, 9);
        return ResponseEntity.ok(result);
    }

}
