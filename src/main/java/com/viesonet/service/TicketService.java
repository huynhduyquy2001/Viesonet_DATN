package com.viesonet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.Acl.User;
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

    public Ticket buyTicket(Users user, int ticket, float totalAmount) {
        // Kiểm tra xem có bản ghi Ticket với userId tương ứng không
        Ticket newTicket = new Ticket();
        newTicket.setUser(user);
        newTicket.setTicket(ticket);
        newTicket.setBuyDate(new Date());
        newTicket.setTotalAmount(totalAmount);
        return ticketDao.saveAndFlush(newTicket);
    }

    public float getTotalAmountByTicket(Users user) {
        List<Object[]> ticketSumList = ticketDao.getTicketUserId(user.getUserId());

        // Kiểm tra nếu danh sách không trống
        if (!ticketSumList.isEmpty()) {
            // Giả sử phần tử đầu tiên của mảng kết quả là SUM(t.totalAmount)
            Object[] firstResult = ticketSumList.get(0);
            // Giả sử SUM(t.totalAmount) là kiểu Number
            Number totalAmountSum = (Number) firstResult[0];

            // Chuyển đổi totalAmountSum thành int (hoặc bất kỳ kiểu phù hợp nào khác)
            return totalAmountSum.floatValue();
        } else {
            // Nếu danh sách trống, trả về 0 hoặc xử lý theo yêu cầu của ứng dụng của bạn
            return 0;
        }
    }

    public int getUserTicketCount(Users user) {
        Integer ticketCount = ticketDao.getTicketSumByUserId(user.getUserId());

        // Kiểm tra nếu ticketCount không null
        if (ticketCount != null) {
            // Chuyển đổi ticketCount thành int (hoặc bất kỳ kiểu phù hợp nào khác)
            return ticketCount.intValue();
        } else {
            // Nếu ticketCount là null, trả về 0 hoặc xử lý theo yêu cầu của ứng dụng của
            // bạn
            return 0;
        }
    }

}
