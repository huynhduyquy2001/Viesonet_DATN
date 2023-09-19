package com.viesonet.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.OrderDetailsDao;

@Service
public class OrderDetailsService {

    @Autowired
    OrderDetailsDao orderDetailsDao;

    public List<Integer> getProductIdList(List<Integer> list) {
        return orderDetailsDao.getProductIdList(list);
    }
}
