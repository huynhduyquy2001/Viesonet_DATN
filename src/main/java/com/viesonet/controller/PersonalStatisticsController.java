package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Orders;
import com.viesonet.service.OrdersService;
import com.viesonet.service.ProductsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin("*")
public class PersonalStatisticsController {
    @Autowired
    OrdersService ordersService;

    @Autowired
    ProductsService productsService;

    @GetMapping("/personalStatisticsSumTotalAmout")
    public List<Object[]> getOrdersSUMApprovedOrdersByMonth() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ordersService.exeTotalAmountByMonth(userId);
    }

    @GetMapping("/personalStatisticsCoutOrderStatus")
    public List<Object[]> getOrderStatusCountsForOtherBuyers() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ordersService.getOrderStatusCountsForOtherBuyers(userId);
    }

    @GetMapping("/personalStatisticsCoutOrder")
    public List<Object[]> execountApprovedOrdersByMonth() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ordersService.execountApprovedOrdersByMonth(userId);
    }

    @GetMapping("/productbestSelling/{year}")
    public List<Object[]> exeproductBestSelling(@PathVariable int year) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return productsService.exeproductBestSelling(userId, year);
    }

    @GetMapping("/totalamount/{year}")
    public List<Float> getTotalAmount(@PathVariable int year) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ordersService.getTongTien(userId, year);
    }

    @GetMapping("/getyear")
    public List<Integer> getyear() {
        return ordersService.GetYearsFromOrders();
    }

    @GetMapping("/getcoutorder/{year}")
    public List<Integer> GetOrderCountByYear(@PathVariable int year) {
        return ordersService.GetOrderCountByYear(year);
    }

    @GetMapping("/getcoutcancelorder/{year}")
    public List<Integer> GetOrderCacelCountByYear(@PathVariable int year) {
        return ordersService.GetOrderCacelCountByYear(year);
    }
}