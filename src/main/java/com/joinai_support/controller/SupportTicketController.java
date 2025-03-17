package com.joinai_support.controller;

import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.AuthenticationResponse;
import com.joinai_support.dto.TicketStatusDTO;
import com.joinai_support.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket") // Correctly defining the base path for this controller
@CrossOrigin("*")
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @PostMapping("/launchTicket")
    public String launchTicket(@RequestBody SupportTicket supportTicket) {
        return supportTicketService.launchTicket(supportTicket);
    }

    @PutMapping("/updateTicket")
    public ResponseEntity<String> updateTicket(@RequestBody TicketStatusDTO supportTicket) {
        return supportTicketService.updateTicket(supportTicket);
    }

    @GetMapping("/getMyTickets")
    public ResponseEntity<List<SupportTicket>> getMyTickets(@RequestBody AuthenticationResponse authenticationResponse) {
        return supportTicketService.getMyTickets(authenticationResponse);
    }
}
