package com.viesonet.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Promotions")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promotionId;

    private float percentValue;
    private float minimumValue;
    private Date startDate;
    private Date endDate;
    private String code;

}
