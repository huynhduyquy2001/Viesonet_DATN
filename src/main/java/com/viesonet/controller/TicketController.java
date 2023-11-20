package com.viesonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.viesonet.entity.History;
import com.viesonet.entity.TotalTicket;
import com.viesonet.entity.Users;
import com.viesonet.service.TicketService;
import com.viesonet.service.UsersService;

@RestController
@CrossOrigin()
public class TicketController {
    @Autowired
    TicketService ticketService;

    @Autowired
    UsersService usersService;

    @GetMapping("/getUserTicketCount")
    public ResponseEntity<Integer> getUserTicketCount() {
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = usersService.findUserById(userId);

            int ticketCount = ticketService.getUserTicketCount(user);

            return ResponseEntity.ok(ticketCount);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
