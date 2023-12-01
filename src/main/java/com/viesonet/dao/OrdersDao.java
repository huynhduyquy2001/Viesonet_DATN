package com.viesonet.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.viesonet.entity.Orders;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.entity.ViolationsPosts;

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
                        "WHERE u.userId <> :sellerId " + // Chọn các đơn hàng mà userId của người dùng không phải là
                                                         // người bán
                        "AND od.product.productId = p.productId")
        List<Object[]> getPendingConfirmationOrdersForSeller(@Param("sellerId") String sellerId);

        @Query("SELECT obj FROM Orders obj WHERE obj.orderId = :orderID")
        public Orders findCartByOrderId(@Param("orderID") int orderID);

        @Query("SELECT os.statusName, COALESCE(COUNT(o.orderStatus), 0) AS so_don_hang " +
                        "FROM Orders o " +
                        "JOIN o.orderStatus os " +
                        "JOIN o.orderDetails od " +
                        "JOIN od.product p " +
                        "JOIN p.user u " +
                        "WHERE u.userId <> :userId " +
                        "GROUP BY os.statusName")
        List<Object[]> getOrderStatusCountsForOtherBuyers(@Param("userId") String userId);

        // đếm số lượng đơn hàng
        @Procedure("sp_SoLuongDonHangDuyet")
        List<Object[]> execountApprovedOrdersByMonth(String sellerId);

        @Procedure("GetGroupedYearsFromOrders")
        List<Integer> getyear();

        @Procedure("GetOrderCountByYear")
        List<Integer> GetOrderCountByYear(int year);

        @Procedure("GetOrderCacelCountByYear")
        List<Integer> GetOrderCacelCountByYear(int year);

        // doanh thu theo tháng
        @Procedure("sp_doanhThuThang")
        List<Object[]> exeTotalAmountByMonth(String sellerId);

        @Procedure("GetTotalSalesForYearAndCustomer")
        Float exeGetTotalSalesForYearAndCustomer(String sellerId, int year);

        // lấy tổng số tiền theo năm
        @Procedure("YourStoredProcedureName")
        List<Float> getTotalSalesForCustomerByYear(String customerId, int year);
}
