package com.viesonet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ProductColorsDao;
import com.viesonet.entity.Colors;
import com.viesonet.entity.ProductColors;
import com.viesonet.entity.Products;

@Service
public class ProductColorsService {
    @Autowired
    ProductColorsDao productColorsDao;

    public ProductColors saveProductColor(Colors color, Products product, int quantity) {
        ProductColors obj = new ProductColors();
        obj.setColor(color);
        obj.setProduct(product);
        obj.setQuantity(quantity);
        return productColorsDao.saveAndFlush(obj);
    }

    public ResponseEntity<String> minusProduct(Products product, int quantity, String colorName) {

        try {
            ProductColors productColors = productColorsDao.findByProductIdAndColor(product.getProductId(), colorName);
            if (productColors == null) {
                return ResponseEntity.ok("Không có sản phẩm này để trừ số lượng!");
            } else {
                productColors.setQuantity(productColors.getQuantity() - quantity);
                productColorsDao.saveAndFlush(productColors);
                return ResponseEntity.ok("Trừ số lượng thành công");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    public ProductColors findByProductIdAndColor(Products product, String color) {
        return productColorsDao.findByProductIdAndColor(product.getProductId(), color);
    }
}
