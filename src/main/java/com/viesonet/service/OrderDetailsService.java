package com.viesonet.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.rpc.context.AttributeContext.Response;
import com.viesonet.dao.OrderDetailsDao;
import com.viesonet.entity.OrderDetails;
import com.viesonet.entity.Orders;
import com.viesonet.entity.ProductColors;
import com.viesonet.entity.Products;
import com.viesonet.entity.ShoppingCart;

@Service
public class OrderDetailsService {

    @Autowired
    OrderDetailsDao orderDetailsDao;

    public List<Integer> getProductIdList(List<Integer> list) {
        return orderDetailsDao.getProductIdList(list);
    }

    public ResponseEntity<String> addOrderDetail(int orderId, Products product, int quantity, float originalPrice,
            float salePrice, String color) {
        try {
            OrderDetails ods = new OrderDetails();
            Orders o = new Orders();
            o.setOrderId(orderId);
            ods.setOrder(o);
            ods.setProduct(product);
            ods.setQuantity(quantity);
            ods.setOriginalPrice(originalPrice);
            ods.setSalePrice(salePrice);
            ods.setColor(color);
            orderDetailsDao.saveAndFlush(ods);
            return ResponseEntity.ok("Thành công!");
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.ok("Thất bại!");
        }
    }
}
