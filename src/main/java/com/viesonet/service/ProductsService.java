package com.viesonet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viesonet.dao.ProductStatusDao;
import com.viesonet.dao.ProductsDao;
import com.viesonet.dao.ProductsTempDao;
import com.viesonet.entity.Posts;
import com.viesonet.entity.ProductColors;
import com.viesonet.entity.Products;
import com.viesonet.entity.ProductsTemp;
import com.viesonet.entity.Violations;
import com.viesonet.dao.ColorsDao;
import com.viesonet.dao.ProductColorsDao;
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
    ProductStatusDao productStatusDao;

    @Autowired
    ProductColorsDao productColorsDao;

    @Autowired
    ProductsTempDao productsTempDao;

    public Page<Products> findProductByName(int page, String key) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "datePost"));
        Page<Products> list = productsDao.findProductByName(pageable, key);

        if (list.isEmpty()) {
            // Nếu danh sách kết quả rỗng, tách chuỗi thành các từ và tìm kiếm theo từng từ
            String[] words = key.split("\\s+");
            Map<Integer, Products> resultMap = new HashMap<>();

            for (String word : words) {
                Page<Products> wordList = productsDao.findProductByName(pageable, word);
                if (!wordList.isEmpty()) {
                    // Lặp qua danh sách từng từ để kiểm tra sản phẩm và thêm vào Map nếu chưa có
                    for (Products product : wordList.getContent()) {
                        if (!resultMap.containsKey(product.getProductId())) {
                            resultMap.put(product.getProductId(), product);
                        }
                    }
                }
            }

            // Tạo một danh sách mới từ Map resultMap
            List<Products> resultList = new ArrayList<>(resultMap.values());

            // Tạo một Page từ danh sách kết quả
            Page<Products> finalPage = new PageImpl<>(resultList, pageable, resultList.size());
            return finalPage;
        }

        return list;
    }

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

    public Page<Products> findPostsProductMyStore(int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);
        return productsDao.findPostsProductMyStore(pageable, userId);
    }

    public Page<Products> findPostsProductPending(int page, int size, String userId) {
        Pageable pageable = PageRequest.of(page, size);
        return productsDao.findPostsProductPending(pageable, userId);
    }

    public List<Products> getRelatedProducts(String userId) {
        return productsDao.getRelatedProducts(userId);
    }

    public Page<Object> findPostsProductWithDecline(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productsDao.findPostsProductWithDecline(pageable);
    }

    public List<Object> findSearchProducts(String name) {
        return productsDao.findSearchProducts(name);
    }

    public Page<Products> findSearchProductMyStore(String name, String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productsDao.findSearchProductMyStore(name, userId, pageable);
    }

    public Products findProductById(int productId) {
        Optional<Products> optionalProduct = productsDao.findById(productId);
        return optionalProduct.orElse(null);
    }

    public void acceptByProductId(int productId) {
        Products products = productsDao.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm bên bảng sản phẩm"));
        ProductsTemp product = (ProductsTemp) productsTempDao.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm bên bảng tạm"));

        products.setProductStatus(productStatusDao.findById(1).orElse(null));

        productsDao.saveAndFlush(products);
        productsTempDao.delete(product);
    }

    public List<Products> getAllProducts() {
        return productsDao.findAll();
    }

    public Products addProduct(Products product, Users userId) {
        ProductStatus p = new ProductStatus();
        product.setUser(userId);
        product.setDatePost(new Date());

        // Kiểm tra nếu product.getProductStatus() != null và getStatusId() == 1
        if (product.getProductStatus() != null) {
            p.setStatusId(1);
        } else {
            p.setStatusId(3);
        }

        product.setProductStatus(p);
        return productsDao.saveAndFlush(product);
    }

    public ResponseEntity<String> hideProductMyStore(int productId) {
        try {
            Products products = productsDao.findByProductId(productId);

            ProductStatus p = new ProductStatus();
            p.setStatusId(2);
            products.setProductStatus(p);
            productsDao.saveAndFlush(products);
            return ResponseEntity.ok("Đã ẩn sản phẩm.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @Transactional // Thêm @Transactional trước phương thức
    public List<Object[]> exeproductBestSelling(String sellerId, int year) {
        return productsDao.exeproductBestSelling(sellerId, year);
    }

    public Page<Products> findProductFavoritByName(int page, String keyF) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Products> list = productsDao.findProductByName(pageable, keyF);
        if (list.isEmpty()) {
            // Nếu danh sách kết quả rỗng, tách chuỗi thành các từ và tìm kiếm theo từng từ
            String[] words = keyF.split("\\s+");
            Map<Integer, Products> resultMap = new HashMap<>();

            for (String word : words) {
                Page<Products> wordList = productsDao.findProductByName(pageable, word);
                if (!wordList.isEmpty()) {
                    // Lặp qua danh sách từng từ để kiểm tra sản phẩm và thêm vào Map nếu chưa có
                    for (Products product : wordList.getContent()) {
                        if (!resultMap.containsKey(product.getProductId())) {
                            resultMap.put(product.getProductId(), product);
                        }
                    }
                }
            }

            // Tạo một danh sách mới từ Map resultMap
            List<Products> resultList = new ArrayList<>(resultMap.values());

            // Tạo một Page từ danh sách kết quả
            Page<Products> finalPage = new PageImpl<>(resultList, pageable, resultList.size());
            return finalPage;
        }
        return list;
    }

}
