package com.viesonet.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.RatingsDao;
import com.viesonet.entity.Products;
import com.viesonet.entity.Ratings;

@Service
public class RatingsService {
    @Autowired
    RatingsDao ratingsDao;

    public Double getAverageRating(int productId) {
        return ratingsDao.getAverageRating(productId);
    }

    public Ratings rateProduct(Ratings rating, Products product) {
        rating.setProduct(product);
        rating.setRatingDate(new Date());
        return ratingsDao.saveAndFlush(rating);
    }
}
