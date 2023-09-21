package com.viesonet.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    // @GetMapping("/getshopping")
    // private List<Products> getShopping() {

    // String userId =
    // SecurityContextHolder.getContext().getAuthentication().getName();
    // List<Follow> followList = followService.getFollowing(userId);
    // List<String> followedUserIds = followList.stream()
    // .map(follow -> follow.getFollowing().getUserId())
    // .collect(Collectors.toList());
    // Page<Posts> allFollowedPosts =
    // productsService.findPostsByListUserId(followedUserIds);
    // System.out.println("Do dai: " + list.size());
    // return list;
    // }

    @GetMapping("/get-shopping-by-page/{page}")
    public Page<Products> getShoppingByPage(@PathVariable int page) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Follow> followList = followService.getFollowing(userId);
        List<String> followedUserIds = followList.stream()
                .map(follow -> follow.getFollowing().getUserId())
                .collect(Collectors.toList());
        Page<Products> list = productsService.getShoppingByPage(followedUserIds, page, 10);
        System.out.println(list);
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
        // lấy danh sách những sản phẩm có trong đơn hàng đó
        List<Integer> ProductIdList = orderDetailsService.getProductIdList(ordersId);
        Page<Products> productList = productsService.getTrendingProducts(ProductIdList, page, 10);
        return productList;
    }

    @PostMapping("/add-to-cart/{productId}")
    public String addShoppingCart(@PathVariable int productId) {
        return "Success";
    }

}
