package com.viesonet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import com.viesonet.dao.ShoppingCartDao;
import com.viesonet.entity.Products;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.entity.Users;

@Service
public class ShoppingCartService {
    @Autowired
    ShoppingCartDao shoppingCartDao;

    public ResponseEntity<String> addToCart(Users user, Products product, int quantity, String color) {
        try {

            ShoppingCart o = shoppingCartDao.findCartByProductId(user.getUserId(), product.getProductId(), color);
            if (o != null) {
                System.out.println("san pham da co trong gio hang");
                o.setQuantity(o.getQuantity() + quantity);
                shoppingCartDao.saveAndFlush(o);
                return ResponseEntity.ok("Sản phẩm đã được tăng số lượng.");
            } else {
                ShoppingCart obj = new ShoppingCart();
                obj.setProduct(product);
                obj.setUser(user);
                obj.setQuantity(quantity);
                obj.setColor(color);
                shoppingCartDao.saveAndFlush(obj);
                return ResponseEntity.ok("Sản phẩm đã được thêm vào giỏ hàng.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi thêm sản phẩm vào giỏ hàng: " + e.getMessage());
        }
    }

    public List<ShoppingCart> findShoppingCartByUserId(String userId) {
        return shoppingCartDao.findCartByUserId(userId);
    }

}
