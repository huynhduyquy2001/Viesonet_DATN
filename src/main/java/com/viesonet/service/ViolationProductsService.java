package com.viesonet.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.ViolationProductsDao;
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;
import com.viesonet.entity.ViolationProducts;

@Service
public class ViolationProductsService {

    @Autowired
    ViolationProductsDao violationProductsDao;

    public ViolationProducts reportProduct(Users user, Products product, String reportContent) {
        ViolationProducts obj = violationProductsDao.findViolationProductsById(product.getProductId(),
                user.getUserId());
        if (obj != null) {
            return obj;
        }
        ViolationProducts newObj = new ViolationProducts();
        newObj.setProduct(product);
        newObj.setUser(user);
        newObj.setReportDate(new Date());
        newObj.setDescription(reportContent);
        newObj.setStatus(false);
        violationProductsDao.saveAndFlush(newObj);
        return newObj;
    }
}
