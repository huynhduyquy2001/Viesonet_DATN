package com.viesonet.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Orders;

public interface OrdersDao extends JpaRepository<Orders, Integer> {

    @Query("SELECT p.orderId FROM Orders p WHERE p.orderDate >= :startDate")
    List<Integer> getShoppingWithinLast7Days(Date startDate);

    @Query(value = "SELECT p.order.orderId FROM OrderDetails p WHERE p.order.orderId IN :orderId GROUP BY p.order.orderId ORDER BY SUM(p.quantity) DESC LIMIT 100")
    List<Integer> getTrending(List<Integer> orderId);

    @Query("SELECT o FROM Orders o JOIN o.orderDetails od WHERE o.customer.userId = :customerId")
    List<Orders> findOrdersByCustomerId(String customerId);

}
