package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/myOrders")
    public List<Object[]> getOrdersWithProducts() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("USERID: " + userId);
        // String userId = "NEBY24305636";
        return ordersService.findOrdersByCustomerId(userId);
    }

    @GetMapping("/pending-confirmation")
    public List<Object[]> getOrdersPendingConfirmation() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ordersService.getPendingConfirmationOrdersForSeller(userId);
    }

    @PostMapping("/approveorders/{orderID}")
    public ResponseEntity<String> approveorders(@PathVariable("orderID") int orderId) {
        try {
            ordersService.approveorders(orderId);
            return ResponseEntity.ok("{\"message\": \"Sản phẩm đã được duyệt.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi duyệt sản phẩm : " + e.getMessage());
        }
    }

    @PostMapping("/acceptOrders/{orderID}")
    public ResponseEntity<String> acceptOrders(@PathVariable("orderID") int orderId) {
        try {
            ordersService.acceptOrders(orderId);
            return ResponseEntity.ok("{\"message\": \"Đã nhận được đơn hàng.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi nhận sản phẩm: " + e.getMessage());
        }
    }

    @PostMapping("/cancelOrders/{orderID}")
    public ResponseEntity<String> cancelOrders(@PathVariable("orderID") int orderId) {
        try {
            ordersService.cancelOrders(orderId);
            return ResponseEntity.ok("{\"message\": \"Đã nhận hủy đơn hàng.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi hủy đơn hàng: " + e.getMessage());
        }
    }
}
