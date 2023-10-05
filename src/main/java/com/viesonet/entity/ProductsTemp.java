package com.viesonet.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ProductsTemp")
public class ProductsTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tempId;
    private int productId;
    private String productName;
    private float originalPrice;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePost;
    private String userName;

}

