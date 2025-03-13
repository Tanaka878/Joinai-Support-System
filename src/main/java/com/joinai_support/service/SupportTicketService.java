package com.joinai_support.service;


import com.joinai_support.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;

    @Autowired
    public SupportTicketService(SupportTicketRepository supportTicketRepository) {
        this.supportTicketRepository = supportTicketRepository;
    }
}
