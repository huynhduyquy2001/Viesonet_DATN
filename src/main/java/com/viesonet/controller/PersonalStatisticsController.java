package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Orders;
import com.viesonet.service.OrdersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin("*")
public class PersonalStatisticsController {
    @Autowired
    OrdersService ordersService;

    @GetMapping("/personalStatistics")
    public List<Object[]> getOrderscountApprovedOrdersByMonth() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ordersService.getcountApprovedOrdersByMonth(userId);
    }

}
