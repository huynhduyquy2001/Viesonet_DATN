package com.viesonet.entity;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.Column;
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
@Table(name = "ViolationProducts")
@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViolationsProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int violationId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Products product;

    
    private String userId;
    private String description;
    private Boolean status;

}
