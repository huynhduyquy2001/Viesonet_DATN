package com.viesonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Accounts;
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;
import com.viesonet.service.AccountsService;
import com.viesonet.service.OrderDetailsService;
import com.viesonet.service.OrdersService;
import com.viesonet.service.ProductsService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin("*")
public class MyStoreController {
    @Autowired
    ProductsService productsService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    AccountsService accountService;

    @Autowired
    UsersService userDAO;

    @GetMapping("/mystore/{userId}/{page}")
    public Page<Products> getShoppingByPage(@PathVariable int page, @PathVariable String userId) {
        return productsService.findPostsProductMyStore(page, 9, userId);
    }

    @GetMapping("/mystore-pending/{userId}/{page}")
    public Page<Products> getShoppingByPagePending(@PathVariable int page, @PathVariable String userId) {
        return productsService.findPostsProductPending(page, 9, userId);
    }

    @GetMapping("/searchProductMyStore/{userId}/{search}")
    public Page<Products> searchProductMyStore(@PathVariable String search, @PathVariable String userId) {
        return productsService.findSearchProductMyStore(search, userId, 0, 9);
    }

    @PostMapping("/hideProductMyStore/{userId}/{productId}/{page}")
    public Page<Products> searchProductMyStore(@PathVariable int productId, @PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            productsService.hideProductMyStore(productId);
        } catch (Exception e) {

        }
        return productsService.findPostsProductMyStore(page, 9, userId);
    }

    @PutMapping("/myStore/userRole/{sdt}/{role}")
    public Users userRole(@PathVariable int role, @PathVariable String sdt) {
        String roleName = "";
        if (role == 2) {
            roleName = "Staff";
        } else if (role == 3) {
            roleName = "User";
        }
        Accounts accounts = new Accounts();
        accounts = accountService.setRole(sdt, role, roleName);
        accounts = accountService.findByPhoneNumber(sdt);
        return userDAO.findUserById(accounts.getUser().getUserId());
    }
}
