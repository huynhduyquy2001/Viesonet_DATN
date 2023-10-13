package com.viesonet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DeliveryAddress")
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private int ProvinceId;
    private String ProvinceName;
    private int DistrictId;
    private String DistrictName;
    private String WardCode;
    private String WardName;
    private String deliveryPhone;
    private boolean addressStore;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;

    private String DetailAddress;
}
