package com.viesonet.controller;

import java.util.Map;

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

import com.viesonet.service.RejectProductsService;

@RestController
@CrossOrigin("*")
public class RejectProductsController {
    @Autowired
    RejectProductsService rejectProductsService;

    @GetMapping("staff/rejectproducts/{Page}")
    public ResponseEntity<Page<Object>> getProduct(@PathVariable int Page) {
        Page<Object> result = rejectProductsService.getProducts(Page, 10);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/staff/rejectproduct/reject/{productId}")
    public ResponseEntity<Object> accept(@PathVariable int productId, @RequestBody Map<String, String> body) {
        String reason = body.get("reason");
        if (reason == null || reason.isEmpty()) {
            return ResponseEntity.badRequest().body("Lỗi: Lý do không được để trống");
        }
        rejectProductsService.rejectProduct(productId, reason);
        Page<Object> result = rejectProductsService.getProducts(0, 10);
        return ResponseEntity.ok(result);
    }

}
