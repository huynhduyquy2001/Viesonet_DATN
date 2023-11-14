package com.viesonet.dao;

import com.viesonet.entity.Ticket;
import com.viesonet.entity.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketDao extends JpaRepository<Ticket, Integer> {

    @Query("SELECT SUM(t.totalAmount) AS totalAmount, SUM(t.ticket) AS ticket, t.ticketId, t.buyDate, t.user.userId " +
            "FROM Ticket t " +
            "WHERE t.user.userId = :userId " +
            "GROUP BY t.ticketId, t.buyDate, t.user.userId, t.ticket")
    List<Object[]> getTicketUserId(@Param("userId") String userId);

    @Query("SELECT SUM(t.ticket) FROM Ticket t WHERE t.user.userId = :userId")
    Integer getTicketSumByUserId(@Param("userId") String userId);
}