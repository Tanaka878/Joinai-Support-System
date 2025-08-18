package com.joinai_support.service;


import com.joinai_support.domain.TicketAnalysis;
import com.joinai_support.repository.TicketAnalysisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TicketAnalysisService {

    private final TicketAnalysisRepository repository;

    public TicketAnalysisService(TicketAnalysisRepository repository) {
        this.repository = repository;
    }

    // Create a new ticket
    public TicketAnalysis createRecord(String ticketId, String question) {
        TicketAnalysis ticket = new TicketAnalysis(ticketId, question);
        return repository.save(ticket);
    }

    // Add a reply to an existing ticket
    @Transactional
    public TicketAnalysis addReply(String ticketId, String reply) {
        Optional<TicketAnalysis> ticketOpt = repository.findById(ticketId);
        if (ticketOpt.isPresent()) {
            TicketAnalysis ticket = ticketOpt.get();
            ticket.addReply(reply);
            return repository.save(ticket);
        } else {
            throw new RuntimeException("Ticket with ID " + ticketId + " not found");
        }
    }

    // Fetch a ticket
    public Optional<TicketAnalysis> getTicket(String ticketId) {
        return repository.findById(ticketId);
    }
}
