package com.viesonet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ProductsDao;
import com.viesonet.entity.Products;

@Service
public class FavoriteProductService {
    @Autowired
    ProductsDao ProductDAO;

    public List<Products> findFavoriteProductsByUserId(String userId) {
        return ProductDAO.findFavoriteProductsByUserId(userId);
    }
}
