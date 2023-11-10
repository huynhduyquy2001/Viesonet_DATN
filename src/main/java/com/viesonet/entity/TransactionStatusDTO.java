package com.viesonet.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionStatusDTO implements Serializable {
    private String status;
    private String message;
    private String date;
    private String amount;
}
