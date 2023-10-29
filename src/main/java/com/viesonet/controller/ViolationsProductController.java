package com.viesonet.controller;

import java.util.List;

import org.jcodec.common.DictionaryCompressor.Int;
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

import com.viesonet.entity.Notifications;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Products;
import com.viesonet.entity.ViolationsProduct;
import com.viesonet.service.ViolationsProductService;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin("*")
public class ViolationsProductController {

    @Autowired
    private ViolationsProductService violationsProductService;

    @GetMapping("/staff/violationsproduct/{Page}")
    public ResponseEntity<Page<Object>> getProduct(@PathVariable int Page) {
        Page<Object> result = violationsProductService.findProductWithFalse(Page, 10);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/staff/alreadyviolationsproduct/{Page}")
    public ResponseEntity<Page<Object>> getProductViolation(@PathVariable int Page) {
        Page<Object> result = violationsProductService.findProductWithTrue(Page, 10);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/staff/violationsproduct/countViolations/{productId}")
    public ResponseEntity<Integer> countViolations(@PathVariable int productId) {
        int result = violationsProductService.countViolations(productId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/staff/violationsproduct/detailProduct/{violationId}")
    public ViolationsProduct getViolationsByPostId(@PathVariable int violationId) {
        return violationsProductService.findProductById(violationId);
    }

    @GetMapping("/staff/violationsproduct/listDescription/{productId}")
    public ResponseEntity<List<String>> getDescriptionByProductId(@PathVariable int productId) {
        List<String> result = violationsProductService.getDescriptionByProductId(productId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/staff/violationsproduct/getProductId/{violationId}")
    public Integer getProductId(@PathVariable int violationId) {
        return violationsProductService.getProductId(violationId);
    }

    @GetMapping("/staff/searchViolationProduct")
    public ResponseEntity<List<Object>> searchProductViolation(@RequestParam String name) {
        List<Object> searchResult = violationsProductService.findSearchProducts(name);
        return ResponseEntity.ok(searchResult);
    }

    @GetMapping("/staff/searchAlreadyViolationProduct")
    public ResponseEntity<List<Object>> searchAlreadyProductViolation(@RequestParam String name) {
        List<Object> searchResult = violationsProductService.findSearchViolationProducts(name);
        return ResponseEntity.ok(searchResult);
    }

    @PostMapping("/staff/violationsproduct/accept/{productId}")
    @Transactional
    public ResponseEntity<Page<Object>> acceptProductViolations(@PathVariable int productId) {
        violationsProductService.acceptByPostViolations(productId);
        Page<Object> result = violationsProductService.findProductWithFalse(0, 10);
        return ResponseEntity.ok(result);

    }

    @PostMapping("/staff/violationsproduct/delete/{productId}")
    @Transactional
    public ResponseEntity<Page<Object>> deleteProductViolations(@PathVariable int productId) {
        violationsProductService.deleteViolationProduct(productId);
        Page<Object> result = violationsProductService.findProductWithFalse(0, 10);
        return ResponseEntity.ok(result);

    }
}
