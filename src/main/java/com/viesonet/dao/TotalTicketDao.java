package com.viesonet.dao;

import com.viesonet.entity.History;
import com.viesonet.entity.TotalTicket;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

public interface TotalTicketDao extends JpaRepository<TotalTicket, Integer> {

    @Query("SELECT t.ticket FROM TotalTicket t WHERE t.user.userId = :userId")
    Integer findTicketByUserId(@Param("userId") String userId);

    @Query("SELECT t.ticketId FROM TotalTicket t WHERE t.user.userId = :userId")
    Integer findTicketIdByUserId(@Param("userId") String userId);

    @Query("SELECT t FROM TotalTicket t WHERE t.user.userId = :userId")
    Optional<TotalTicket> findExistTicketByUserId(@Param("userId") String userId);

    @Query("SELECT SUM(t.totalAmount) AS totalAmount FROM History t")
    Object reportTopTicket();

    @Query("SELECT SUM(t.totalAmount) AS totalAmount FROM History t WHERE MONTH(t.buyDate) =:buyDate")
    Object reportTicketByMonth(int buyDate);

    @Query("SELECT COUNT(t.historyId) AS totalTicket FROM History t WHERE MONTH(t.buyDate) =:buyDate")
    Object reportCountTicketByMonth(int buyDate);

    @Query("SELECT COUNT(DISTINCT t.user.userId) AS totalUsers FROM History t  WHERE MONTH(t.buyDate) =:buyDate")
    Object reportCountUserByMonth(int buyDate);

}