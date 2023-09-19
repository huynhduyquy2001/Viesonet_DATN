package com.viesonet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ProductsDao;
import com.viesonet.entity.Products;

@Service
public class ProductsService {
    @Autowired
    ProductsDao productsDao;

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

    public Page<Object> findPostsProductWithProcessing(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		return productsDao.findPostsProductWithProcessing(pageable);
	}
}
