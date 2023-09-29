package com.viesonet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.service.ProductsService;
import com.viesonet.service.ShoppingCartService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin("*")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    ProductsService productsService;

    @Autowired
    UsersService usersService;

    @GetMapping("/get-product-shoppingcart")
    public List<ShoppingCart> getProductByShoppingCart() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return shoppingCartService.findShoppingCartByUserId(userId);
    }

    @PostMapping("/setQuantity-to-cart")
    public ResponseEntity<String> setQuantityToCart(@RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity, @RequestParam("color") String color) {
        // Xử lý dữ liệu productId và quantity
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            shoppingCartService.setQuantityToCart(usersService.getById(userId), productsService.getProduct(productId),
                    quantity, color);
            return ResponseEntity.ok("Sản phẩm đã được thay đổi số lượng trong giỏ hàng");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi sửa số lượng sản phẩm trong giỏ hàng: " + e.getMessage());
        }
    }

    @PostMapping("/minusQuantity-to-cart")
    public ResponseEntity<String> minusQuantityToCart(@RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity, @RequestParam("color") String color) {
        // Xử lý dữ liệu productId và quantity
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            shoppingCartService.minusQuantityToCart(usersService.getById(userId), productsService.getProduct(productId),
                    quantity, color);
            return ResponseEntity.ok("Sản phẩm đã được thay đổi số lượng trong giỏ hàng");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi sửa số lượng sản phẩm trong giỏ hàng: " + e.getMessage());
        }
    }

}
