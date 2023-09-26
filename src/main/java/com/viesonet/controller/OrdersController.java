package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Orders;
import com.viesonet.entity.Products;
import com.viesonet.service.AccountsService;
import com.viesonet.service.OrdersService;
import com.viesonet.service.ProductsService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin("*")
public class OrdersController {
    @Autowired
    ProductsService productsService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    UsersService usersService;

    @Autowired
    AccountsService accountsService;

    @GetMapping("/orders")
    public List<Object[]> getOrdersWithProducts() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("USERID: " + userId);
        // String userId = "NEBY24305636";
        return ordersService.findOrdersByCustomerId(userId);
    }

}
