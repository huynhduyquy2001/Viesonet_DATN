package com.viesonet.dao;

import com.viesonet.entity.Ticket;
import com.viesonet.entity.Users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketDao extends JpaRepository<Ticket, Integer> {

    @Query("SELECT t FROM Ticket t WHERE t.user.userId = :userId")
    Optional<Ticket> findUsersByTicket(@Param("userId") String userId);
}
