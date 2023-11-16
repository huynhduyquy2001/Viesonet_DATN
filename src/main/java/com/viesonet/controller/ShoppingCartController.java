package com.viesonet.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viesonet.entity.DeliveryAddress;
import com.viesonet.entity.Notifications;
import com.viesonet.entity.Orders;
import com.viesonet.entity.Products;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.service.DeliveryAddressService;
import com.viesonet.service.FavoriteProductService;
import com.viesonet.service.NotificationsService;
import com.viesonet.service.OrderDetailsService;
import com.viesonet.service.OrdersService;
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
    OrdersService ordersService;

    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    NotificationsService notificationsService;

    @Autowired
    FavoriteProductService favoriteProductService;

    @Autowired
    UsersService usersService;

    @Autowired
    DeliveryAddressService deliveryAddressService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/get-product-shoppingcart")
    public List<ShoppingCart> getProductByShoppingCart() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return shoppingCartService.findShoppingCartByUserId(userId);
    }

    @GetMapping("/get-address")
    public List<DeliveryAddress> getAddress() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return deliveryAddressService.getAddress(userId);
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

    @PostMapping("/orderShoppingCart")
    public List<ShoppingCart> orderToCart(@RequestBody List<Map<String, String>> requestData) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Integer> productIdList = new ArrayList<>();
        List<String> colorList = new ArrayList<>();
        try {
            for (Map<String, String> data : requestData) {
                String productId = data.get("productId");
                String color = data.get("color");
                try {
                    int productIdInt = Integer.parseInt(productId);
                    productIdList.add(productIdInt);
                    colorList.add(color);
                } catch (NumberFormatException e) {
                    // Xử lý nếu productId không phải là số
                }
            }
            return shoppingCartService.getListProductToCart(userId, productIdList, colorList);
        } catch (Exception e) {
            e.printStackTrace(); // log lỗi
            return new ArrayList<>(); // trả về giá trị mặc định khác
        }
    }

    @PostMapping("/add-to-DeliveryAddress")
    public List<DeliveryAddress> addToDeliveryAddress(@RequestParam("provinceID") int provinceID,
            @RequestParam("deliveryPhone") String deliveryPhone,
            @RequestParam("provinceName") String provinceName, @RequestParam("districtID") int districtID,
            @RequestParam("districtName") String districtName, @RequestParam("wardCode") String wardCode,
            @RequestParam("wardName") String wardName, @RequestParam("detailAddress") String detailAddress,
            @RequestParam("addressStore") boolean addressStore) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        // thêm địa chỉ
        deliveryAddressService.addDeliveryAddress(districtID, provinceID, wardCode, districtName, provinceName,
                wardName, detailAddress, usersService.getUserById(userId), deliveryPhone, addressStore);

        return deliveryAddressService.getAddress(userId);
    }

    @PostMapping("/deleteAddress/{id}")
    public List<DeliveryAddress> deleteAddress(@PathVariable int id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        deliveryAddressService.deleteAddress(id);

        return deliveryAddressService.getAddress(userId);
    }

    @GetMapping("/get-oneAddress/{id}")
    public DeliveryAddress getOneAddress(@PathVariable int id) {
        return deliveryAddressService.getOneAddress(id);
    }

    @GetMapping("/get-listAddress")
    public List<DeliveryAddress> getListAddress(@RequestParam List<String> userIds) {
        return deliveryAddressService.getListAddress(userIds);
    }

    @PostMapping("/payShoppingCart/{totalAmount}/{address}/{shipfee}")
    public ResponseEntity<ResponseEntity<String>> payToCart(@RequestBody List<Map<String, String>> requestData,
            @PathVariable float totalAmount, @PathVariable String address, @PathVariable float shipfee) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Integer> productIdList = new ArrayList<>();
        List<String> colorList = new ArrayList<>();
        try {
            for (Map<String, String> data : requestData) {
                String productId = data.get("productId");
                String color = data.get("color");

                try {
                    int productIdInt = Integer.parseInt(productId);
                    productIdList.add(productIdInt);
                    colorList.add(color);
                } catch (NumberFormatException e) {
                    // Xử lý nếu productId không phải là số
                }
            }
            // Tìm danh sách sản phẩm
            List<ShoppingCart> shoppingCart = shoppingCartService.getListProductToCart(userId, productIdList,
                    colorList);

            // Trừ số lượng trong Product
            for (int i = 0; i < shoppingCart.size(); i++) {
                productsService.minusProduct(shoppingCart.get(i).getProduct(), shoppingCart.get(i).getQuantity());
            }

            // Lưu vào order
            Orders orders = ordersService.addOrder(userId, address, totalAmount, shipfee);

            for (int i = 0; i < shoppingCart.size(); i++) {
                // Lưu vào orderDetails
                float sale = shoppingCart.get(i).getProduct().getOriginalPrice()
                        - (shoppingCart.get(i).getProduct().getOriginalPrice()
                                * shoppingCart.get(i).getProduct().getPromotion()) / 100;
                orderDetailsService.addOrderDetail(orders.getOrderId(), shoppingCart.get(i).getProduct(),
                        shoppingCart.get(i).getQuantity(),
                        shoppingCart.get(i).getProduct().getOriginalPrice(), sale, shoppingCart.get(i).getColor());
                // Xóa các sản phẩm đã thêm vào orderDetail ra khỏi giỏ hàng
                shoppingCartService.deleteToCart(String.valueOf(shoppingCart.get(i).getProduct().getProductId()),
                        userId, shoppingCart.get(i).getColor());
            }

            // thêm thông báo
            String previousUserId = "";

            for (int i = 0; i < shoppingCart.size(); i++) {
                String currentUserId = shoppingCart.get(i).getProduct().getUser().getUserId();
                if (currentUserId.equals(previousUserId) == false) {
                    System.out.println("1");
                    // thêm thông báo
                    Notifications notifications = notificationsService.createNotifications(
                            usersService.findUserById(userId), 0,
                            shoppingCart.get(i).getProduct().getUser(), null, 7);

                    messagingTemplate.convertAndSend("/private-user", notifications);

                    // Update the previous user ID
                    previousUserId = currentUserId;
                }
            }

            return ResponseEntity.ok(ResponseEntity.ok("Thanh toán thành công đơn hàng đang chờ xác nhận"));
        } catch (Exception e) {
            e.printStackTrace(); // log lỗi
            return ResponseEntity.ok(ResponseEntity.ok("Lỗi"));
        }
    }
}