package com.viesonet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.rpc.context.AttributeContext.Response;
import com.viesonet.dao.HistoryDao;
import com.viesonet.dao.TotalTicketDao;
import com.viesonet.dao.UsersDao;
import com.viesonet.entity.History;
import com.viesonet.entity.TotalTicket;
import com.viesonet.entity.Users;

import jakarta.transaction.Transactional;

@Service
public class TicketService {
    @Autowired
    TotalTicketDao ticketDao;

    @Autowired
    UsersDao usersDao;

    @Autowired
    HistoryDao historyDao;

    public ResponseEntity<?> buyTicket(Users user, int ticket, float totalAmount) {
        // Kiểm tra xem có bản ghi TotalTicket với userId tương ứng không
        Optional<TotalTicket> existingTotalTicket = ticketDao.findExistTicketByUserId(user.getUserId());
        // Lấy thông tin về vé từ TicketDao
        Integer totalTicket = ticketDao.findTicketIdByUserId(user.getUserId());
        if (existingTotalTicket.isPresent()) {
            // Nếu đã tồn tại, cộng thêm vào giá trị đã mua
            TotalTicket currentTotalTicket = existingTotalTicket.get();
            currentTotalTicket.setTicket(currentTotalTicket.getTicket() + ticket);
            ticketDao.saveAndFlush(currentTotalTicket);

            // Tạo một lịch sử mới
            History newTicket = new History();
            newTicket.setUser(user);
            newTicket.setTicketId(totalTicket);
            newTicket.setTicketCount(ticket);
            newTicket.setBuyDate(new Date());
            newTicket.setTotalAmount(totalAmount);
            historyDao.saveAndFlush(newTicket);

            return ResponseEntity.status(HttpStatus.OK).body("Ticket purchased successfully.");
        } else {
            // Nếu chưa tồn tại, tạo mới một bản ghi TotalTicket
            TotalTicket newTotalTicket = new TotalTicket();
            newTotalTicket.setUser(user);
            newTotalTicket.setTicket(ticket);
            ticketDao.saveAndFlush(newTotalTicket);

            // Tạo một lịch sử mới
            History newTicket = new History();
            newTicket.setUser(user);
            newTicket.setTicketId(totalTicket);
            newTicket.setTicketCount(ticket);
            newTicket.setBuyDate(new Date());
            newTicket.setTotalAmount(totalAmount);
            historyDao.saveAndFlush(newTicket);

            return ResponseEntity.status(HttpStatus.OK).body("Ticket purchased successfully.");
        }
    }

    public int getUserTicketCount(Users user) {
        Integer ticketCount = ticketDao.findTicketByUserId(user.getUserId());

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

    public int getTicket(String userId) {
        Integer ticketCount = ticketDao.findTicketByUserId(userId);

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

    // Thống kê người mua lượt đăng bài
    public Object reportTopTicket() {
        return ticketDao.reportTopTicket();
    }

    public Object reportTicketBuyMonth(int buyDate) {
        return ticketDao.reportTicketByMonth(buyDate);
    }

    public Object reportCountTicketBuyMonth(int buyDate) {
        return ticketDao.reportCountTicketByMonth(buyDate);
    }

    public Object reportCountUserBuyMonth(int buyDate) {
        return ticketDao.reportCountUserByMonth(buyDate);
    }

}
