package com.viesonet.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Orders;
import com.viesonet.entity.ShoppingCart;

public interface OrdersDao extends JpaRepository<Orders, Integer> {

        @Query("SELECT p.orderId FROM Orders p WHERE p.orderDate >= :startDate")
        List<Integer> getShoppingWithinLast7Days(Date startDate);

        @Query(value = "SELECT p.order.orderId FROM OrderDetails p WHERE p.order.orderId IN :orderId GROUP BY p.order.orderId ORDER BY SUM(p.quantity) DESC LIMIT 100")
        List<Integer> getTrending(List<Integer> orderId);

        @Query("SELECT o, od, p ,u FROM Orders o " +
                        "JOIN o.orderDetails od " +
                        "JOIN od.product p " +
                        "JOIN p.user u " +
                        "WHERE o.customer.userId = :customerId")
        List<Object[]> findOrdersByCustomerId(@Param("customerId") String customerId);

        @Query("SELECT o, p, od, os, u FROM Orders o " +
                        "JOIN o.customer u " +
                        "JOIN o.orderStatus os " +
                        "JOIN o.orderDetails od " +
                        "JOIN od.product p " +
                        "WHERE u.userId = :sellerId " +
                        "AND od.product.productId = p.productId")
        List<Object[]> getPendingConfirmationOrdersForSeller(@Param("sellerId") String sellerId);

        @Query("SELECT obj FROM Orders obj WHERE obj.orderId = :orderID")
        public Orders findCartByOrderId(@Param("orderID") int orderID);

        // đếm số lượng đơn hàng đã duyệt
        @Query(value = "SELECT t.thang, COALESCE(COUNT(o.OrderId), 0) AS doanh_thu_thang "
                        + "FROM (SELECT 1 AS thang UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 "
                        + "UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 "
                        + "UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) t "
                        + "LEFT JOIN Orders o ON MONTH(o.OrderDate) = t.thang "
                        + "LEFT JOIN OrderStatus os ON o.StatusId = os.StatusId "
                        + "LEFT JOIN OrderDetails od ON o.OrderId = od.OrderId "
                        + "LEFT JOIN Products p ON od.ProductId = p.ProductId "
                        + "LEFT JOIN Users u ON u.UserId = p.UserId "
                        + "AND os.StatusId = 4 AND u.UserId = :sellerId "
                        + "GROUP BY t.thang ORDER BY t.thang", nativeQuery = true)
        List<Object[]> countApprovedOrdersByMonth(@Param("sellerId") String sellerId);

}
