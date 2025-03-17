package com.joinai_support.service;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.AuthenticationResponse;
import com.joinai_support.dto.TicketStatusDTO;
import com.joinai_support.repository.SupportTicketRepository;
import com.joinai_support.springSecurity.config.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final AdminService adminService;
    private final JWTService jwtService;


    @Autowired
    public SupportTicketService(SupportTicketRepository supportTicketRepository, AdminService adminService, JWTService jwtService) {
        this.supportTicketRepository = supportTicketRepository;
        this.adminService = adminService;
        this.jwtService = jwtService;
    }

    public String launchTicket(SupportTicket supportTicket) {
        // Step 1: Fetch all admins
        ResponseEntity<List<Admin>> agentsResponse = adminService.getAll();
        List<Admin> agentList = agentsResponse.getBody();

        if (agentList == null || agentList.isEmpty()) {
            return "No admins available to assign the ticket.";
        }

        Admin selectedAdmin = agentList.stream()
                .min(Comparator.comparingInt(admin -> admin.getTickets().size()))
                .orElseThrow(() -> new IllegalStateException("Failed to find an admin."));

        supportTicket.setAssignedTo(selectedAdmin);
        supportTicketRepository.save(supportTicket);

        return "Ticket successfully assigned to admin: " + selectedAdmin.getId();
    }


    public ResponseEntity<String> updateTicket(TicketStatusDTO supportTicket) {
        Optional<SupportTicket> supportTicketEntity = supportTicketRepository.findById(supportTicket.getTicketId());
        if (supportTicketEntity.isPresent()) {
            supportTicketEntity.get().setStatus(supportTicket.getStatus());
            supportTicketRepository.save(supportTicketEntity.get());
            return ResponseEntity.ok("Ticket successfully updated.");
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<SupportTicket>> getMyTickets(AuthenticationResponse authenticationResponse) {
       String email =  jwtService.extractUserName(authenticationResponse.getToken());
       Admin admin =adminService.getAdmin(email);
       List<SupportTicket> tickets = admin.getTickets();
       return ResponseEntity.ok(tickets);
    }
}