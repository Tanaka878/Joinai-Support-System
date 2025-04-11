package com.joinai_support.service;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.*;
import com.joinai_support.repository.AdminRepository;
import com.joinai_support.repository.SupportTicketRepository;
import com.joinai_support.utils.Authenticate;
import com.joinai_support.utils.Status;
import com.joinai_support.utils.TicketDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final AdminService adminService;
    private final AdminRepository adminRepository;


    @Autowired
    public SupportTicketService(SupportTicketRepository supportTicketRepository, AdminService adminService, AdminRepository adminRepository) {
        this.supportTicketRepository = supportTicketRepository;
        this.adminService = adminService;
        this.adminRepository = adminRepository;
    }

    public String launchTicket(SupportTicket supportTicket) {
        // Fetch all available admins
        ResponseEntity<List<Admin>> agentsResponse = adminService.getAll();
        List<Admin> agentList = agentsResponse.getBody();

        // Check if admins are available
        if (agentList == null || agentList.isEmpty()) {
            return "No admins available to assign the ticket.";
        }

        // Find the admin with the least number of tickets
        Admin selectedAdmin = agentList.stream()
                .filter(admin -> admin.getTickets() != null) // Ensure tickets are not null
                .min(Comparator.comparingInt(admin -> admin.getTickets().size()))
                .orElse(null);

        if (selectedAdmin == null) {
            return "Failed to find a suitable admin for ticket assignment.";
        }

        // Assign the ticket to the selected admin
        supportTicket.setAssignedTo(selectedAdmin);
        supportTicket.setLaunchTimestamp(LocalDateTime.now());
        supportTicket.setStatus(Status.NEW); // Ensure the ticket starts with a default status

        try {
            supportTicketRepository.save(supportTicket);
        } catch (Exception e) {
            // Log the exception and return a failure message
            e.printStackTrace();
            return "Failed to save the support ticket. Please try again.";
        }

        return "Ticket successfully assigned to admin: " + selectedAdmin.getId();
    }



    @Transactional
    public ResponseEntity<String> updateTicket(TicketStatusDTO supportTicket) {
        Optional<SupportTicket> supportTicketEntity = supportTicketRepository.findById(supportTicket.getTicketId());
        if (supportTicketEntity.isPresent()) {
            supportTicketEntity.get().setStatus(supportTicket.getStatus());
            Duration between = Duration.between(supportTicketEntity.get().getLaunchTimestamp(), LocalDateTime.now());
            supportTicketEntity.get().setTimeLimit(between);
            supportTicketEntity.get().setServedTimestamp(LocalDateTime.now());
            supportTicketRepository.save(supportTicketEntity.get());

            return ResponseEntity.ok("Ticket successfully updated.");
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<SupportTicket>> getMyTickets(Authenticate authenticationResponse) {
      // List<SupportTicket> tickets = userValidator.getUser(authenticationResponse.getToken()).getTickets();
        List<SupportTicket> tickets= adminRepository.findByEmail(authenticationResponse.getToken()).getTickets();
       return ResponseEntity.ok(tickets);
    }

    public ResponseEntity<StatisticsDTO> getStatistics() {
        double avgTimeLimit = supportTicketRepository.findAll()
                .parallelStream()
                .mapToDouble(supportTicket -> supportTicket.getTimeLimit().toSeconds())
                .average()
                .orElse(0);
        StatisticsDTO statisticsDTO = new StatisticsDTO();
        statisticsDTO.setAvgResolveTime(avgTimeLimit);
        return ResponseEntity.ok(statisticsDTO);
    }

    //method for calculating statics for agents for use by agents
    public ResponseEntity<StatsByAgent> getStatsByAgent(Admin admin) {
        List<SupportTicket> tickets = supportTicketRepository.findAllByAssignedTo(admin);
        StatsByAgent statsByAgent = new StatsByAgent();

        LocalDateTime now = LocalDateTime.now();

        // Daily stats (last 24 hours)
        LocalDateTime dailyStart = now.minusHours(24);
        statsByAgent.setDAILY_TICKETS(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isAfter(dailyStart) ||
                                supportTicket.getLaunchTimestamp().isEqual(dailyStart))
                .count());

        statsByAgent.setSOLVED_DAILY(tickets.stream()
                .filter(supportTicket ->
                        (supportTicket.getLaunchTimestamp().isAfter(dailyStart) ||
                                supportTicket.getLaunchTimestamp().isEqual(dailyStart)) &&
                                (supportTicket.getStatus() == Status.CLOSED))
                .count());

        // Weekly stats (last 7 days / 168 hours)
        LocalDateTime weeklyStart = now.minusHours(168);
        statsByAgent.setWEEKLY_TICKETS(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isAfter(weeklyStart) ||
                                supportTicket.getLaunchTimestamp().isEqual(weeklyStart))
                .count());

        statsByAgent.setSOLVED_WEEKLY(tickets.stream()
                .filter(supportTicket ->
                        (supportTicket.getLaunchTimestamp().isAfter(weeklyStart) ||
                                supportTicket.getLaunchTimestamp().isEqual(weeklyStart)) &&
                                (supportTicket.getStatus() == Status.CLOSED))
                .count());

        // Monthly stats (last 28 days / 672 hours)
        LocalDateTime monthlyStart = now.minusHours(672);
        statsByAgent.setMONTHLY_TICKETS(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isAfter(monthlyStart) ||
                                supportTicket.getLaunchTimestamp().isEqual(monthlyStart))
                .count());

        statsByAgent.setSOLVED_MONTHLY(tickets.stream()
                .filter(supportTicket ->
                        (supportTicket.getLaunchTimestamp().isAfter(monthlyStart) ||
                                supportTicket.getLaunchTimestamp().isEqual(monthlyStart)) &&
                                (supportTicket.getStatus() == Status.CLOSED))
                .count());

        return ResponseEntity.ok(statsByAgent);
    }
    public ResponseEntity<List<TicketDTO>> getNotifications(String email) {
        List<TicketDTO> notifications = new ArrayList<>(); // Use a mutable list
        List<SupportTicket> tickets = adminRepository.findByEmail(email).getTickets();

        tickets.forEach(supportTicket -> {
            TicketDTO ticketDTO = new TicketDTO(); // Create a new instance for each ticket
            ticketDTO.setId(supportTicket.getId());
            ticketDTO.setStatus(supportTicket.getStatus());
            ticketDTO.setLaunchTimestamp(supportTicket.getLaunchTimestamp());
            ticketDTO.setCategory(supportTicket.getCategory());
            ticketDTO.setPriority(supportTicket.getPriority());
            ticketDTO.setAttachments(supportTicket.getAttachments());
            ticketDTO.setSubject(supportTicket.getSubject());

            notifications.add(ticketDTO); // Add the DTO to the mutable list
        });

        return ResponseEntity.ok(notifications);
    }

//stats for admins

}