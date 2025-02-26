package com.springAI.SupprtTicket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService) {
        this.supportTicketService = supportTicketService;
    }

}
