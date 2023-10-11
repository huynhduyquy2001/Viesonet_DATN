package com.viesonet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.FavoriteProductsDao;
import com.viesonet.dao.ProductsDao;
import com.viesonet.entity.FavoriteProducts;
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;

@Service
public class FavoriteProductService {
    @Autowired
    ProductsDao ProductDAO;
    @Autowired
    FavoriteProductsDao favoriteProductsDao;

    public List<Products> findFavoriteProductsByUserId(String userId) {
        return ProductDAO.findFavoriteProductsByUserId(userId);
    }

    public boolean getFavoriteProducts(String userId, int productId) {
        FavoriteProducts obj = favoriteProductsDao.findFavoriteProduct(userId, productId);
        if (obj != null) {
            return true;
        }
        return false;
    }

    public FavoriteProducts addFavoriteProduct(Users user, Products product) {
        FavoriteProducts obj = favoriteProductsDao.findFavoriteProduct(user.getUserId(), product.getProductId());
        if (obj == null) {
            obj = new FavoriteProducts(); // Tạo đối tượng mới nếu không tìm thấy
            obj.setFavoriteDate(new Date());
            obj.setProduct(product);
            obj.setUser(user);
            return favoriteProductsDao.saveAndFlush(obj);
        } else {
            favoriteProductsDao.delete(obj);
        }
        return obj;
    }

}
