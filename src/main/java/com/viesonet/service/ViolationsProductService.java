package com.viesonet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ViolationsProductDao;
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;
import com.viesonet.entity.Violations;
import com.viesonet.entity.ViolationsProduct;

@Service
public class ViolationsProductService {

    @Autowired
    ViolationsProductDao violationsProductDao;

    public ViolationsProduct reportProduct(Users user, Products product, String reportContent) {
        ViolationsProduct obj = violationsProductDao.findViolationProductsById(product.getProductId(),
                user.getUserId());
        if (obj != null) {
            return obj;
        }
        ViolationsProduct newObj = new ViolationsProduct();
        newObj.setProduct(product);
        newObj.setUserId(user.getUserId());
        newObj.setReportDate(new Date());
        newObj.setDescription(reportContent);
        newObj.setStatus(false);
        violationsProductDao.saveAndFlush(newObj);
        return newObj;
    }

    public Page<Object> findProductWithFalse(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return violationsProductDao.findProductWithFalse(pageable);
    }

    public Page<Object> findProductWithTrue(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return violationsProductDao.findProductWithTrue(pageable);
    }

    public ViolationsProduct findProductById(int violationId) {
        Optional<ViolationsProduct> optionalProduct = violationsProductDao.findById(violationId);
        return optionalProduct.orElse(null);
    }

    public List<String> getDescriptionByProductId(int productId) {
        return violationsProductDao.getDescriptionByProductId(productId);
    }

    public int countViolations(int productId) {
        return violationsProductDao.countViolations(productId);
    }

    public int getProductId(int violationId) {
        return violationsProductDao.getProductId(violationId);
    }

    public List<Object> findSearchProducts(String name) {
        return violationsProductDao.findSearchProducts(name);
    }

    public List<Object> findSearchViolationProducts(String name) {
        return violationsProductDao.findSearchViolationProducts(name);
    }

    public void acceptByPostViolations(Integer productId) {

        List<ViolationsProduct> violations = violationsProductDao.findByProductId(productId);
        for (ViolationsProduct violation : violations) {
            violation.setStatus(true);
            violationsProductDao.saveAllAndFlush(violations);

        }
    }

    public void deleteViolationProduct(Integer productId) {
        List<ViolationsProduct> violations = violationsProductDao.findByProductId(productId);
        for (ViolationsProduct violation : violations) {
            violationsProductDao.delete(violation);
        }
    }

}
