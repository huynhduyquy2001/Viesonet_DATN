package com.viesonet.service;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.viesonet.dao.DeliveryAddressDao;
import com.viesonet.entity.DeliveryAddress;
import com.viesonet.entity.Users;

@Service
public class DeliveryAddressService {
    @Autowired
    DeliveryAddressDao deliveryAddressDao;

    public List<DeliveryAddress> getAddress(String userId) {
        return deliveryAddressDao.findByDeliveryAddress(userId);
    }

    public DeliveryAddress getOneAddress(int id) {
        return deliveryAddressDao.findById(id);
    }

    public List<DeliveryAddress> getListAddress(List<String> userId) {
        return deliveryAddressDao.findByListDeliveryAddress(userId);
    }

    public ResponseEntity<String> addDeliveryAddress(int districtID, int provinceID, String wardCode,
            String districtName, String provinceName, String wardName, String detailAddress, Users user,
            String deliveryPhone, boolean addressStore) {

        try {
            DeliveryAddress d = new DeliveryAddress();
            d.setDistrictId(districtID);
            d.setDistrictName(districtName);
            d.setProvinceId(provinceID);
            d.setProvinceName(provinceName);
            d.setWardCode(wardCode);
            d.setWardName(wardName);
            d.setDetailAddress(detailAddress);
            d.setDeliveryPhone(deliveryPhone);
            d.setUser(user);
            d.setAddressStore(addressStore);
            deliveryAddressDao.saveAndFlush(d);
            return ResponseEntity.ok("Thêm địa chỉ thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Có lỗi: " + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteAddress(int id) {
        try {
            DeliveryAddress d = deliveryAddressDao.findById(id);
            deliveryAddressDao.delete(d);
            return ResponseEntity.ok("Xóa địa chỉ thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Có lỗi: " + e.getMessage());
        }
    }
}
