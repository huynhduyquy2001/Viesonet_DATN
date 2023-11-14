package com.viesonet.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.apache.http.annotation.Contract;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.Http;
import com.viesonet.config.PaymentConfig;
import com.viesonet.entity.Orders;
import com.viesonet.entity.PaymentResDTO;
import com.viesonet.entity.ShoppingCart;
import com.viesonet.entity.Ticket;
import com.viesonet.entity.TransactionStatusDTO;
import com.viesonet.service.OrderDetailsService;
import com.viesonet.service.OrdersService;
import com.viesonet.service.ShoppingCartService;
import com.viesonet.service.TicketService;
import com.viesonet.service.UsersService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    TicketService ticketService;

    @Autowired
    UsersService usersService;

    List<Integer> productIdList = new ArrayList<>();
    List<String> colorList = new ArrayList<>();
    private static float totalAmount;
    private static String deliveryAdress;
    private static float shipFee;
    private static String currentUserId;
    private static int checkTransaction = 0;
    private static int ticketCount;

    @PostMapping("/create_payment_shoppingcart/{amount}/{address}/{shipfee}")
    public ResponseEntity<?> create_payment_shoppingcart(
            @RequestBody List<Map<String, String>> requestData,
            @PathVariable int amount,
            @PathVariable String address,
            @PathVariable float shipfee)
            throws UnsupportedEncodingException {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String orderType = "other";
        totalAmount = amount;
        deliveryAdress = address;
        shipFee = shipfee;
        currentUserId = userId;
        checkTransaction = 1;
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

        // long amount = Integer.parseInt(req.getParameter("amount")) * 100;
        // String bankCode = req.getParameter("bankCode");
        long totalPrice = Long.parseLong(String.valueOf(amount));
        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalPrice * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;
        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setUrl(paymentUrl);
        System.out.println(paymentResDTO);

        return ResponseEntity.ok(paymentResDTO);
    }

    @PostMapping("/create_payment_ticket/{ticket}/{amount}")
    public ResponseEntity<?> create_payment_ticket(
            @PathVariable int ticket,
            @PathVariable int amount)
            throws UnsupportedEncodingException {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String orderType = "other";
        ticketCount = ticket;
        totalAmount = amount;
        currentUserId = userId;
        checkTransaction = 2;

        long totalPrice = Long.parseLong(String.valueOf(amount));
        String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConfig.vnp_Version);
        vnp_Params.put("vnp_Command", PaymentConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(totalPrice * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;
        PaymentResDTO paymentResDTO = new PaymentResDTO();
        paymentResDTO.setUrl(paymentUrl);
        System.out.println(paymentResDTO);

        return ResponseEntity.ok(paymentResDTO);
    }

    @GetMapping("/payment-callback")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_Amount") String vnpAmount,
            @RequestParam(value = "vnp_BankCode") String vnpBankCode,
            @RequestParam(value = "vnp_OrderInfo") String vnpOrderInfo,
            @RequestParam(value = "vnp_ResponseCode") String vnpResponseCode,
            HttpServletResponse httpResponse) {

        // Kiểm tra vnpResponseCode để xác định trạng thái thanh toán
        if ("00".equals(vnpResponseCode) && checkTransaction == 1) {
            // Giao dịch thành công
            // Thực hiện các xử lý cập nhật CSDL
            // Tạo đơn hàng và lưu vào CSDL
            Orders orders = ordersService.addOrder(currentUserId, deliveryAdress, totalAmount, shipFee);

            // Lấy danh sách sản phẩm từ giỏ hàng
            List<ShoppingCart> shoppingCart = shoppingCartService.getListProductToCart(currentUserId, productIdList,
                    colorList);

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
                        currentUserId, shoppingCart.get(i).getColor());
            }
            totalAmount = 0;
            deliveryAdress = null;
            shipFee = 0;
        }

        if ("00".equals(vnpResponseCode) && checkTransaction == 2) {
            ticketService.buyTicket(usersService.findUserById(currentUserId), ticketCount, totalAmount);
            totalAmount = 0;
            ticketCount = 0;
        }
        String responseJSON = null;
        if ("00".equals(vnpResponseCode) && checkTransaction == 1) {
            // Chuyển sang trang đơn hàng
            responseJSON = "<script>window.location.href='http://127.0.0.1:5501/Index.html#!/order/" +
                    currentUserId
                    + "';</script>";
        } else if ("00".equals(vnpResponseCode) && checkTransaction == 2) {
            responseJSON = "<script>window.location.href='http://127.0.0.1:5501/Index.html#!/mystore/"
                    + currentUserId + "/0';</script>";
        } else {
            // Chuyển sang trang giỏ hàng
            responseJSON = "<script>window.location.href='http://127.0.0.1:5501/Index.html#!';</script>";
        }
        checkTransaction = 0;
        return ResponseEntity.status(HttpStatus.OK).body(responseJSON);
    }

}
