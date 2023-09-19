package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.Notifications;
import com.viesonet.entity.OrderDetails;
import com.viesonet.entity.Products;

public interface OrderDetailsDao extends JpaRepository<OrderDetails, Integer> {

    @Query("SELECT p.product.productId FROM OrderDetails p WHERE p.order.orderId IN :orderId GROUP BY p.product.productId ORDER BY SUM(p.quantity) DESC")
    List<Integer> getProductIdList(List<Integer> orderId);

}
