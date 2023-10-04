package com.viesonet.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.service.FavoriteProductService;
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
    FavoriteProductService favoriteProductService;

    @Autowired
    UsersService usersService;

    @GetMapping("/get-product-shoppingcart")
    public List<ShoppingCart> getProductByShoppingCart() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return shoppingCartService.findShoppingCartByUserId(userId);
    }

    @PostMapping("/setQuantity-to-cart")
    public ResponseEntity<ResponseEntity<String>> setQuantityToCart(@RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity, @RequestParam("color") String color) {
        // Xử lý dữ liệu productId và quantity
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponseEntity<String> response = shoppingCartService.setQuantityToCart(usersService.getById(userId),
                productsService.getProduct(productId),
                quantity, color);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/minusQuantity-to-cart")
    public ResponseEntity<ResponseEntity<String>> minusQuantityToCart(@RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity, @RequestParam("color") String color) {
        // Xử lý dữ liệu productId và quantity
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponseEntity<String> res = shoppingCartService.minusQuantityToCart(usersService.getById(userId),
                productsService.getProduct(productId),
                quantity, color);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/addFavouriteProducts")
    public ResponseEntity<Map<String, Object>> addFavoriteProduct(@RequestBody List<String> productIds) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ResponseEntity<Map<String, Object>> response = favoriteProductService.addListFavoriteProduct(productIds,
                userId);
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/deleteToCart")
    public ResponseEntity<Map<String, Object>> deleteToCart(@RequestBody List<Map<String, String>> requestData) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Map<String, Object> result = new HashMap<>();
            for (Map<String, String> data : requestData) {
                String productId = data.get("productId");
                String color = data.get("color");
                shoppingCartService.deleteToCart(productId, userId, color);
            }
            result.put("status", "success");
            result.put("message", "Xóa thành công " + requestData.size() + " sản phẩm khỏi giỏ hàng");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResult);
        }
    }

}
