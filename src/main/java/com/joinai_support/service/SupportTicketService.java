package com.joinai_support.service;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.*;
import com.joinai_support.repository.AdminRepository;
import com.joinai_support.repository.SupportTicketRepository;
import com.joinai_support.utils.Authenticate;
import com.joinai_support.utils.MailSenderService;
import com.joinai_support.utils.Priority;
import com.joinai_support.utils.Status;
import com.joinai_support.utils.TicketDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SupportTicketService {
    private static final Logger logger = LoggerFactory.getLogger(SupportTicketService.class);

    private final SupportTicketRepository supportTicketRepository;
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final MailSenderService mailSenderService;


    @Autowired
    public SupportTicketService(SupportTicketRepository supportTicketRepository, 
                               AdminService adminService, 
                               AdminRepository adminRepository,
                               MailSenderService mailSenderService) {
        this.supportTicketRepository = supportTicketRepository;
        this.adminService = adminService;
        this.adminRepository = adminRepository;
        this.mailSenderService = mailSenderService;
    }

    @Transactional
    public String launchTicket(SupportTicket supportTicket) {
        // Fetch all available admins
        ResponseEntity<List<Admin>> agentsResponse = adminService.getAll();
        List<Admin> agentList = agentsResponse.getBody();

        if (agentList == null || agentList.isEmpty()) {
            logger.warn("No admins available to assign the ticket");
            return "No admins available to assign the ticket.";
        }

        // Find the admin with the least number of tickets
        Admin selectedAdmin = agentList.stream()
                .filter(admin -> admin.getTickets() != null) // Ensure tickets are not null
                .min(Comparator.comparingInt(admin -> admin.getTickets().size()))
                .orElse(null);

        if (selectedAdmin == null) {
            logger.warn("Failed to find a suitable admin for ticket assignment");
            return "Failed to find a suitable admin for ticket assignment.";
        }

        // Assign the ticket to the selected admin
        supportTicket.setAssignedTo(selectedAdmin);
        supportTicket.setLaunchTimestamp(LocalDateTime.now());

        Priority[] priorities = Priority.values();

        int randomIndex = new Random().nextInt(priorities.length);
        supportTicket.setPriority(priorities[randomIndex]);
        supportTicket.setStatus(Status.OPEN);

        // Ensure subject is set if not already provided (subject contains issuer info)
        if (supportTicket.getSubject() == null || supportTicket.getSubject().isEmpty()) {
            logger.warn("Ticket created without subject (which contains issuer info). Notifications to issuer will not be possible.");
        }

        try {
            supportTicketRepository.save(supportTicket);

            // Send email notification to the assigned admin
            try {
                mailSenderService.sendTicketCreationNotification(supportTicket, selectedAdmin);
                logger.info("Ticket creation notification sent to admin: {}", selectedAdmin.getEmail());
            } catch (Exception e) {
                // Log the exception but don't fail the ticket creation
                logger.error("Failed to send ticket creation notification to admin: {}", selectedAdmin.getEmail(), e);
            }

            // Send email notification to the customer who opened the ticket
            try {
                mailSenderService.sendTicketOpenedNotification(supportTicket);
                logger.info("Ticket creation notification sent to customer: {}", supportTicket.getSubject());
            } catch (Exception e) {
                // Log the exception but don't fail the ticket creation
                logger.error("Failed to send ticket creation notification to customer: {}", supportTicket.getSubject(), e);
            }

        } catch (Exception e) {
            // Log the exception and return a failure message
            logger.error("Failed to save the support ticket", e);
            return "Failed to save the support ticket. Please try again.";
        }

        return "Ticket successfully assigned to admin: " + selectedAdmin.getId();
    }



    @Transactional
    public ResponseEntity<String> updateTicket(TicketStatusDTO supportTicket) {
        Optional<SupportTicket> supportTicketEntity = supportTicketRepository.findById(supportTicket.getTicketId());
        if (supportTicketEntity.isPresent()) {
            SupportTicket ticket = supportTicketEntity.get();

            // Update ticket status
            ticket.setStatus(supportTicket.getStatus());
            Duration between = Duration.between(ticket.getLaunchTimestamp(), LocalDateTime.now());
            ticket.setTimeLimit(between);
            ticket.setServedTimestamp(LocalDateTime.now());

            // Save the updated ticket
            supportTicketRepository.save(ticket);

            // Send email notification to the assigned admin
            Admin assignedAdmin = ticket.getAssignedTo();
            if (assignedAdmin != null) {
                try {
                    mailSenderService.sendTicketUpdateNotification(ticket, assignedAdmin);
                    logger.info("Ticket update notification sent to admin: {}", assignedAdmin.getEmail());
                } catch (Exception e) {
                    // Log the exception but don't fail the ticket update
                    logger.error("Failed to send ticket update notification to admin: {}", assignedAdmin.getEmail(), e);
                }
            } else {
                logger.warn("No admin assigned to ticket ID: {}, cannot send notification", ticket.getId());
            }

            // Send notification to ticket issuer if the ticket is being closed
            if (ticket.getStatus() == Status.CLOSED) {
                try {
                    mailSenderService.sendTicketClosedNotification(ticket);
                    logger.info("Ticket closed notification sent to issuer: {}", ticket.getSubject());
                } catch (Exception e) {
                    // Log the exception but don't fail the ticket update
                    logger.error("Failed to send ticket closed notification to issuer: {}", ticket.getSubject(), e);
                }
            }

            return ResponseEntity.ok("Ticket successfully updated.");
        }
        else {
            logger.warn("Ticket not found with ID: {}", supportTicket.getTicketId());
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
            // Subject field contains the issuer information, so we use it for issuerEmail as well
            ticketDTO.setIssuerEmail(supportTicket.getSubject());

            notifications.add(ticketDTO); // Add the DTO to the mutable list
        });

        return ResponseEntity.ok(notifications);
    }

//stats for admins

}
