package com.joinai_support.controller;

import com.joinai_support.domain.SupportTicket;
import com.joinai_support.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

    @PostMapping("/launchTicket")
    public String launchTicket(@RequestBody SupportTicket supportTicket) {
        return null;
    }

}
