package com.viesonet.dao;

import com.viesonet.entity.Ticket;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface TicketDao extends JpaRepository<Ticket, Integer> {

    @Query("SELECT SUM(t.totalAmount) AS totalAmount, SUM(t.ticket) AS ticket, t.ticketId, t.buyDate, t.user.userId " +
            "FROM Ticket t " +
            "WHERE t.user.userId = :userId " +
            "GROUP BY t.ticketId, t.buyDate, t.user.userId, t.ticket")
    List<Object[]> getTicketUserId(@Param("userId") String userId);

    @Query("SELECT SUM(t.ticket) FROM Ticket t WHERE t.user.userId = :userId")
    Integer getTicketSumByUserId(@Param("userId") String userId);

    @Query("SELECT SUM(t.totalAmount) AS totalAmount FROM Ticket t")
    Object reportTopTicket();

    @Query("SELECT SUM(t.totalAmount) AS totalAmount FROM Ticket t WHERE MONTH(t.buyDate) =:buyDate")
    Object reportTicketByMonth(int buyDate);

    @Query("SELECT COUNT(t.ticketId) AS totalTicket FROM Ticket t WHERE MONTH(t.buyDate) =:buyDate")
    Object reportCountTicketByMonth(int buyDate);

    @Query("SELECT COUNT(DISTINCT t.user.userId) AS totalUsers FROM Ticket t WHERE MONTH(t.buyDate) =:buyDate")
    Object reportCountUserByMonth(int buyDate);

}