package com.viesonet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.viesonet.entity.DeliveryAddress;

public interface DeliveryAddressDao extends JpaRepository<DeliveryAddress, String> {
    @Query("SELECT obj from DeliveryAddress obj where obj.user.userId =:userId")
    List<DeliveryAddress> findByDeliveryAddress(String userId);

    DeliveryAddress findById(int id);
}
