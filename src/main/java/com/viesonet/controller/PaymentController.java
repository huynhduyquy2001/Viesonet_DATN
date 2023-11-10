package com.viesonet.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.http.HttpStatus;

import org.apache.http.annotation.Contract;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.Http;
import com.viesonet.config.PaymentConfig;
import com.viesonet.entity.PaymentResDTO;
import com.viesonet.entity.TransactionStatusDTO;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
public class PaymentController {

    private Object responseJSON;

    @PostMapping("/create_payment/{amount}")
    public ResponseEntity<ResponseEntity<String>> create_payment(@PathVariable int amount)
            throws UnsupportedEncodingException {

        String orderType = "other";
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
        paymentResDTO.setStatus("ok");
        paymentResDTO.setMessage("Successfully");
        paymentResDTO.setUrl(paymentUrl);

        // com.google.gson.JsonObject job = new JsonObject();
        // job.addProperty("code", "00");
        // job.addProperty("message", "success");
        // job.addProperty("data", paymentUrl);
        // Gson gson = new Gson();
        // resp.getWriter().write(gson.toJson(job));
        // ResponseEntity.ok(paymentUrl);
        return ResponseEntity.ok(ResponseEntity.ok(paymentUrl));
    }

    @GetMapping("/payment-callback")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "vnp_Amount") String vnpAmount,
            @RequestParam(value = "vnp_BankCode") String vnpBankCode,
            @RequestParam(value = "vnp_OrderInfo") String vnpOrderInfo,
            @RequestParam(value = "vnp_ResponseCode") String vnpResponseCode,
            HttpServletResponse httpResponse) {
        TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        // Kiểm tra vnpResponseCode để xác định trạng thái thanh toán
        if ("00".equals(vnpResponseCode)) {
            // Giao dịch thành công
            transactionStatusDTO.setStatus("ok");
            transactionStatusDTO.setMessage("Succesfully");
            transactionStatusDTO.setDate("");
            transactionStatusDTO.setAmount(vnpAmount);
            // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
        } else {
            // Giao dịch thất bại
            transactionStatusDTO.setStatus("no");
            transactionStatusDTO.setMessage("Failed");
            transactionStatusDTO.setDate("");
            transactionStatusDTO.setAmount(vnpAmount);
            // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL

        }
        // Tạo JSON response
        // String responseJSON = "{\"status\":\"" + transactionStatusDTO.getStatus() +
        // "\", \"message\":\""
        // + transactionStatusDTO.getMessage() + "\", \"date\":\"" +
        // transactionStatusDTO.getDate()
        // + "\", \"amount\":\"" + vnpAmount + "\"}";

        if ("00".equals(vnpResponseCode)) {
            String responseJSON = "<script>window.location.href='http://127.0.0.1:5501/Index.html#!/order/" + userId
                    + "';</script>";
            return ResponseEntity.status(HttpStatus.OK).body(responseJSON);
        } else {
            String responseJSON = "<script>window.location.href='http://127.0.0.1:5501/Index.html#!/shoppingcart';</script>";
            return ResponseEntity.status(HttpStatus.OK).body(responseJSON);
        }

    }

}
