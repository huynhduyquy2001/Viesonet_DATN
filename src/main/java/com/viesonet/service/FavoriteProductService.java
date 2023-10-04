package com.viesonet.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.viesonet.dao.FavoriteProductDao;
import com.viesonet.dao.ProductsDao;
import com.viesonet.entity.FavoriteProducts;
import com.viesonet.entity.Products;
import com.viesonet.entity.Users;

@Service
public class FavoriteProductService {
    @Autowired
    ProductsDao ProductDAO;

    @Autowired
    FavoriteProductDao favoriteProductDao;

    public List<Products> findFavoriteProductsByUserId(String userId) {
        return ProductDAO.findFavoriteProductsByUserId(userId);
    }

    public ResponseEntity<Map<String, Object>> addListFavoriteProduct(List<String> productIds, String userId) {
        try {
            Map<String, Object> result = new HashMap<>();

            for (String id : productIds) {
                int productId = Integer.parseInt(id);
                FavoriteProducts fp = favoriteProductDao.findByUserIdAndProductId(userId, productId);

                if (fp != null) {
                    String productName = fp.getProduct().getProductName();
                    result.put("status", "warning");
                    result.put("message", productName + " đã có trong yêu thích!");
                    return ResponseEntity.ok(result);
                } else {
                    Users user = new Users();
                    Products product = new Products();
                    FavoriteProducts favoriteProduct = new FavoriteProducts();

                    user.setUserId(userId);
                    product.setProductId(productId);

                    favoriteProduct.setUser(user);
                    favoriteProduct.setProduct(product);
                    favoriteProduct.setFavoriteDate(new Date());

                    favoriteProductDao.saveAndFlush(favoriteProduct);
                }
            }

            result.put("status", "success");
            result.put("message", "Sản phẩm đã được thêm vào danh sách yêu thích");
            return ResponseEntity.ok(result);
        } catch (NumberFormatException e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResult);
        } catch (Exception e) {
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("status", "error");
            errorResult.put("message", "Lỗi khi thêm sản phẩm vào danh sách yêu thích: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
        }
    }
}
