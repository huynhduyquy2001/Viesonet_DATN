package com.viesonet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

}
