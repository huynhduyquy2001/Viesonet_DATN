package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.RatingsDao;

@Service
public class RatingsService {
    @Autowired
    RatingsDao ratingsDao;

    public Double getAverageRating(int productId) {
        return ratingsDao.getAverageRating(productId);
    }
}
