package com.viesonet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ProductStatusDao;
import com.viesonet.dao.ProductsDao;
import com.viesonet.dao.ProductsTempDao;
import com.viesonet.dao.RejectProductsDao;
import com.viesonet.entity.Products;
import com.viesonet.entity.ProductsTemp;
import com.viesonet.entity.RejectProducts;

import jakarta.transaction.Transactional;

@Service
public class RejectProductsService {
    @Autowired
    RejectProductsDao rejectProductsDao;

    @Autowired
    ProductsTempDao productsTempDao;

    @Autowired
    ProductsDao productsDao;

    @Autowired
    ProductStatusDao productStatusDao;

    public Page<Object> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return rejectProductsDao.getProducts(pageable);
    }

    @Transactional
    public void rejectProduct(int productId, String reason) {
        // Tìm sản phẩm theo id
        ProductsTemp product = (ProductsTemp) productsTempDao.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm bên bảng tạm"));
        Products products = productsDao.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm bên bảng sản phẩm"));
        // Tạo một sản phẩm từ chối mới với thông tin từ sản phẩm
        products.setProductStatus(productStatusDao.findById(5).orElse(null));
        RejectProducts o = new RejectProducts();
        o.setProductId(products.getProductId());
        o.setProductName(products.getProductName());
        o.setOriginalPrice(products.getOriginalPrice());
        o.setUsername(products.getUser().getUsername());
        o.setDate(product.getDatePost());
        o.setReason(reason);

        // Lưu sản phẩm từ chối vào cơ sở dữ liệu
        productsDao.save(products);
        rejectProductsDao.save(o);

        // Xóa sản phẩm khỏi danh sách sản phẩm
        productsTempDao.delete(product);

    }

}
