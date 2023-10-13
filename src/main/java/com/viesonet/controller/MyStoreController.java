package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Products;
import com.viesonet.service.OrderDetailsService;
import com.viesonet.service.OrdersService;
import com.viesonet.service.ProductsService;

@RestController
@CrossOrigin("*")
public class MyStoreController {
    @Autowired
    ProductsService productsService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailsService orderDetailsService;

    @GetMapping("/get-product-mystore/{page}")
    public Page<Products> getShoppingByPage(@PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return productsService.findPostsProductMyStore(page, 9, userId);
    }

    @GetMapping("/filter-product-mystore/{page}/{sortDirection}/{sortName}")
    public Page<Products> filterShoppingByPage(@PathVariable int page, @PathVariable String sortDirection,
            @PathVariable String sortName) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return productsService.filterPostsProductMyStore(page, 9, userId, sortDirection, sortName);
    }

    @GetMapping("/get-trending-myStore/{page}")
    public Page<Products> getTrending(@PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // lấy danh sách đơn hàng trong 7 ngày gần đây
        List<Integer> ordersId = ordersService.getShoppingWithinLast7Days();
        System.out.println(ordersId.size());
        // lấy danh sách những sản phẩm có trong đơn hàng đó
        List<Integer> ProductIdList = orderDetailsService.getProductIdList(ordersId);
        Page<Products> productList = productsService.getTrendingMyStore(ProductIdList, page, 9, userId);
        return productList;
    }

    @GetMapping("/searchProductMyStore/{search}")
    public Page<Products> searchProductMyStore(@PathVariable String search) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return productsService.findSearchProductMyStore(search, userId, 0, 9);
    }

    @PostMapping("/hideProductMyStore/{productId}/{page}")
    public Page<Products> searchProductMyStore(@PathVariable int productId, @PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            productsService.hideProductMyStore(productId);
        } catch (Exception e) {

        }
        return productsService.findPostsProductMyStore(page, 9, userId);
    }
}
