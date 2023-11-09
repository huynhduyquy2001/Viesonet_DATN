package com.viesonet.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firestore.v1.StructuredQuery.Order;
import com.viesonet.dao.OrderDetailsDao;
import com.viesonet.dao.OrdersDao;
import com.viesonet.entity.OrderDetails;
import com.viesonet.entity.OrderStatus;
import com.viesonet.entity.Orders;
import com.viesonet.entity.Products;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.entity.Users;

@Service
public class OrdersService {
    @Autowired
    OrdersDao ordersDao;

    @Autowired
    OrderDetailsDao orderDetailsDao;

    public List<Integer> getShoppingWithinLast7Days() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startDate = calendar.getTime();
        List<Integer> list = ordersDao.getShoppingWithinLast7Days(startDate);
        return list;
    }

    public List<Integer> getTrending(List<Integer> orderId) {
        List<Integer> list = ordersDao.getTrending(orderId);
        return list;
    }

    public List<Object[]> findOrdersByCustomerId(String customerId) {
        return ordersDao.findOrdersByCustomerId(customerId);
    }

    public boolean checkBought(String userId, int productId) {
        List<OrderDetails> obj = orderDetailsDao.checkBought(userId, productId);
        if (obj.size() > 0) {
            return true;
        }
        return false;
    }

    public List<Object[]> getPendingConfirmationOrdersForSeller(String sellerId) {
        return ordersDao.getPendingConfirmationOrdersForSeller(sellerId);
    }

    // Duyệt đơn háng
    public ResponseEntity<String> approveorders(int orderId) {
        try {
            OrderStatus ost = new OrderStatus();
            Orders o = ordersDao.findCartByOrderId(orderId);
            ost.setStatusId(2);
            o.setOrderStatus(ost);
            ordersDao.saveAndFlush(o);
            System.out.println("orderId :" + orderId);
            return ResponseEntity.ok("Sản phẩm đã được duyệt.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi duyệt sản phẩm : " + e.getMessage());
        }
    }

    @Transactional // Thêm @Transactional trước phương thức
    public List<Object[]> execountApprovedOrdersByMonth(String sellerId) {
        return ordersDao.execountApprovedOrdersByMonth(sellerId);
    }

    @Transactional // Thêm @Transactional trước phương thức
    public List<Object[]> exeTotalAmountByMonth(String sellerId) {
        return ordersDao.exeTotalAmountByMonth(sellerId);
    }

    public List<Object[]> getOrderStatusCountsForOtherBuyers(String sellerId) {
        return ordersDao.getOrderStatusCountsForOtherBuyers(sellerId);
    }

    @Transactional // Thêm @Transactional trước phương thức
    public List<Float> getTongTien(String userId, int year) {
        return ordersDao.getTotalSalesForCustomerByYear(userId, year);
    }

    @Transactional // Thêm @Transactional trước phương thức
    public Float exeGetTotalSalesForYearAndCustomer(String sellerId, int year) {
        return ordersDao.exeGetTotalSalesForYearAndCustomer(sellerId, year);
    }

    @Transactional // Thêm @Transactional trước phương thức
    public List<Integer> GetYearsFromOrders() {
        return ordersDao.getyear();
    }

    @Transactional // Thêm @Transactional trước phương thức
    public List<Integer> GetOrderCountByYear(int year) {
        return ordersDao.GetOrderCountByYear(year);
    }

    @Transactional // Thêm @Transactional trước phương thức
    public List<Integer> GetOrderCacelCountByYear(int year) {
        return ordersDao.GetOrderCacelCountByYear(year);
    }
}
