package com.viesonet.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ColorsDao;
import com.viesonet.dao.ProductsDao;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Colors;
import com.viesonet.entity.ProductStatus;
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;

@Service
public class ProductsService {
    @Autowired
    ProductsDao productsDao;

    @Autowired
    ColorsDao colorsDao;

    @Autowired
    UsersService usersService;

    public Products getProduct(int id) {
        Optional<Products> obj = productsDao.findById(id);
        return obj.orElse(null);
    }

    public List<Products> getShopping(List<String> list) {
        List<Products> shoppingList = productsDao.getShopping(list);
        return shoppingList;
    }

    public Page<Products> getShoppingByPage(List<String> list, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datePost"));
        Page<Products> shoppingList = productsDao.getShoppingByPage(list, pageable);
        return shoppingList;
    }

    public Page<Products> getTrendingProducts(List<Integer> list, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "datePost"));
        Page<Products> shoppingList = productsDao.getTrending(list, pageable);
        return shoppingList;
    }

    public Page<Object> findPostsProductWithProcessing(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productsDao.findPostsProductWithProcessing(pageable);
    }

    public List<Products> getRelatedProducts(String userId) {
        return productsDao.getRelatedProducts(userId);
    }

    public List<Products> getAllProducts() {
        return productsDao.findAll();
    }

    public Products addProduct(Products product, Users userId) {
        ProductStatus p = new ProductStatus();
        product.setUser(userId);
        product.setDatePost(new Date());
        p.setStatusId(3);
        product.setProductStatus(p);
        return productsDao.save(product);
    }

}
