package com.viesonet.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.Follow;
import com.viesonet.entity.OrderDetails;
import com.viesonet.entity.Orders;
import com.viesonet.entity.Posts;
import com.viesonet.entity.Products;
import com.viesonet.service.FollowService;
import com.viesonet.service.OrderDetailsService;
import com.viesonet.service.OrdersService;
import com.viesonet.service.ProductsService;
import com.viesonet.service.RatingsService;
import com.viesonet.service.ShoppingCartService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin("*")
public class ShoppingController {

    @Autowired
    ProductsService productsService;
    @Autowired
    FollowService followService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    RatingsService ratingsService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    UsersService usersService;

    @GetMapping("/get-shopping-by-page/{page}")
    public Page<Products> getShoppingByPage(@PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Follow> followList = followService.getFollowing(userId);
        List<String> followedUserIds = followList.stream()
                .map(follow -> follow.getFollowing().getUserId())
                .collect(Collectors.toList());
        Page<Products> list = productsService.getShoppingByPage(followedUserIds, page, 10);
        return list;
    }   

    @GetMapping("/get-average-rating/{productId}")
    public Double getAverageRating(@PathVariable int productId) {
        return ratingsService.getAverageRating(productId);
    }

    @GetMapping("/get-trending/{page}")
    public Page<Products> getTrending(@PathVariable int page) {
        // lấy danh sách đơn hàng trong 7 ngày gần đây
        List<Integer> ordersId = ordersService.getShoppingWithinLast7Days();
        System.out.println(ordersId.size());
        // lấy danh sách những sản phẩm có trong đơn hàng đó
        List<Integer> ProductIdList = orderDetailsService.getProductIdList(ordersId);
        Page<Products> productList = productsService.getTrendingProducts(ProductIdList, page, 10);
        return productList;
    }

    @GetMapping("/find-product-by-name/{page}")
    public Page<Products> findProductByName(@PathVariable int page, @RequestParam String key) {
        return productsService.findProductByName(page, key);
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addToCart(@RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity, @RequestParam("color") String color) {
        // Xử lý dữ liệu productId và quantity
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            shoppingCartService.addToCart(usersService.getById(userId), productsService.getProduct(productId),
                    quantity, color);
            return ResponseEntity.ok("{\"message\": \"Sản phẩm đã được thêm vào giỏ hàng.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi thêm sản phẩm vào giỏ hàng: " + e.getMessage());
        }
    }

}
