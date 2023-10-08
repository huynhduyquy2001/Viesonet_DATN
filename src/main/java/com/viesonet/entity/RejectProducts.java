package com.viesonet.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RejectProducts")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RejectProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rejectId;
    private int productId;
    private String productName;
    private String username;
    private float originalPrice;
    private Date date;
    private String reason;
}
