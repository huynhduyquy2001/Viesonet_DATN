package com.viesonet.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Temporal;

@Entity
@Table(name = "Images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int historyId;

    @ManyToOne
    @JoinColumn(name = "ticketId")
    @JsonIgnore
    private TotalTicket ticket;

    @Column(name = "ticketId", insertable = false, updatable = false)
    private int ticketId;

    @ManyToOne
    @JoinColumn(name = "userId") // Tên cột tham chiếu trong bảng Ticket
    private Users user; // Thay vì Acl.User, bạn sử dụng Users

    private int ticketCount;
    private float totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date buyDate;
}
