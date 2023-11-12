package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ProductsTempDao;
import com.viesonet.entity.ProductsTemp;

@Service
public class ProductsTempService {

    @Autowired
    ProductsTempDao productsTempDao;

    public Page<Object> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productsTempDao.findProduct(pageable);
    }

    public ProductsTemp addProductTemp(ProductsTemp productsTemp) {
        return productsTempDao.saveAndFlush(productsTemp);
    }

}
