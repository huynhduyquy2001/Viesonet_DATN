package com.viesonet.entity;

import java.util.Date;

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
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViolationProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int violationId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Products product;
    @ManyToOne
    @JoinColumn(name = "userId")
    private Users user;
    private String description;
    private boolean status;
}
