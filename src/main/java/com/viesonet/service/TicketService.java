package com.viesonet.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viesonet.dao.TicketDao;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.Ticket;
import com.viesonet.entity.Users;

@Service
public class TicketService {
    @Autowired
    TicketDao ticketDao;

    @Autowired
    UsersDao usersDao;

    public Ticket buyTicket(Users user, int ticket) {
        // Kiểm tra xem có bản ghi Ticket với userId tương ứng không
        Optional<Ticket> existingTicket = ticketDao.findUsersByTicket(user.getUserId());

        if (existingTicket.isPresent()) {
            // Nếu có, cập nhật số lượng
            Ticket tick = existingTicket.get();
            tick.setTicket(tick.getTicket() + ticket);
            return ticketDao.saveAndFlush(tick);
        } else {
            // Nếu không, tạo mới bản ghi Ticket
            Ticket newTicket = new Ticket();
            newTicket.setUser(user);
            newTicket.setTicket(ticket);
            return ticketDao.saveAndFlush(newTicket);
        }
    }

    public int getUserTicketCount(Users user) {
        Optional<Ticket> ticket = ticketDao.findUsersByTicket(user.getUserId());
        return ticket.map(Ticket::getTicket).orElse(0);
    }
}