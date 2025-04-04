package com.joinai_support.service;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.*;
import com.joinai_support.repository.SupportTicketRepository;
import com.joinai_support.repository.UserRepository;
import com.joinai_support.utils.Authenticate;
import com.joinai_support.utils.Status;
import com.joinai_support.utils.UserValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final AdminService adminService;
    private final UserValidator userValidator;


    @Autowired
    public SupportTicketService(SupportTicketRepository supportTicketRepository, AdminService adminService, UserValidator userValidator1) {
        this.supportTicketRepository = supportTicketRepository;
        this.adminService = adminService;
        this.userValidator = userValidator1;

    }

    public String launchTicket(SupportTicket supportTicket) {
        ResponseEntity<List<Admin>> agentsResponse = adminService.getAll();
        List<Admin> agentList = agentsResponse.getBody();

        if (agentList == null || agentList.isEmpty()) {
            return "No admins available to assign the ticket.";
        }

        Admin selectedAdmin = agentList.stream()
                .min(Comparator.comparingInt(admin -> admin.getTickets().size()))
                .orElseThrow(() -> new IllegalStateException("Failed to find an admin."));
        supportTicket.setAssignedTo(selectedAdmin);
        supportTicket.setLaunchTimestamp(LocalDateTime.now());
        supportTicketRepository.save(supportTicket);

        return "Ticket successfully assigned to admin: " + selectedAdmin.getId();
    }


    @Transactional
    public ResponseEntity<String> updateTicket(TicketStatusDTO supportTicket) {
        Optional<SupportTicket> supportTicketEntity = supportTicketRepository.findById(supportTicket.getTicketId());
        if (supportTicketEntity.isPresent()) {
            supportTicketEntity.get().setStatus(supportTicket.getStatus());
            Duration between = Duration.between(supportTicketEntity.get().getLaunchTimestamp(), LocalDateTime.now());
            supportTicketEntity.get().setTimeLimit(between);
            supportTicketRepository.save(supportTicketEntity.get());

            return ResponseEntity.ok("Ticket successfully updated.");
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<SupportTicket>> getMyTickets(Authenticate authenticationResponse) {
       List<SupportTicket> tickets = userValidator.getUser(authenticationResponse.getToken()).getTickets();
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
        Duration durationThreshold = Duration.ofHours(24);
        LocalDateTime deadline = now.minus(durationThreshold);

        statsByAgent.setDAILY_TICKETS(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isBefore(deadline)).count());

        statsByAgent.setSOLVED_DAILY(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isBefore(deadline)&& (supportTicket.getStatus() == Status.CLOSED)).count());


        Duration weeklyDurationThreshold = Duration.ofHours(168);
        LocalDateTime weeklyDeadline = now.minus(weeklyDurationThreshold);

        statsByAgent.setWEEKLY_TICKETS(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isBefore(weeklyDeadline)).count());

        statsByAgent.setSOLVED_WEEKLY(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isBefore(weeklyDeadline) && (supportTicket.getStatus() ==Status.CLOSED)).count());

        Duration monthlyDurationThreshold = Duration.ofHours(672);
        LocalDateTime monthlyDeadline = now.minus(monthlyDurationThreshold);

        statsByAgent.setMONTHLY_TICKETS(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isBefore(monthlyDeadline)).count());

        statsByAgent.setMONTHLY_TICKETS(tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isBefore(monthlyDeadline) && (supportTicket.getStatus() ==Status.CLOSED)).count());
        return ResponseEntity.ok(statsByAgent);
    }

//stats for admins

}