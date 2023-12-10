package com.viesonet.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

// Trong class Ticket
@Entity
@Table(name = "TotalTicket")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TotalTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketId;

    @ManyToOne
    @JoinColumn(name = "userId") // Tên cột tham chiếu trong bảng Ticket
    private Users user; // Thay vì Acl.User, bạn sử dụng Users

    private int ticket;

    @JsonIgnore
    @OneToMany(mappedBy = "ticket")
    private List<History> histories;
}
