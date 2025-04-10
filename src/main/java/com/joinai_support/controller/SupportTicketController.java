package com.joinai_support.controller;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.domain.User;
import com.joinai_support.dto.AuthenticationResponse;
import com.joinai_support.dto.StatisticsDTO;
import com.joinai_support.dto.StatsByAgent;
import com.joinai_support.dto.TicketStatusDTO;
import com.joinai_support.repository.AdminRepository;
import com.joinai_support.repository.UserRepository;
import com.joinai_support.service.SupportTicketService;
import com.joinai_support.utils.Authenticate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ticket")
@CrossOrigin("*")
public class SupportTicketController {

    private final SupportTicketService supportTicketService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService, UserRepository userRepository, AdminRepository adminRepository) {
        this.supportTicketService = supportTicketService;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @PostMapping("/launchTicket")
    public String launchTicket(@RequestBody SupportTicket supportTicket) {
        return supportTicketService.launchTicket(supportTicket);
    }

    @RequestMapping("/updateTicket")
    public ResponseEntity<String> updateTicket(@RequestBody TicketStatusDTO supportTicket) {

        System.out.println(" The user Id"+ supportTicket.getTicketId());
        return supportTicketService.updateTicket(supportTicket);
    }

    @GetMapping("/getMyTickets")
    public ResponseEntity<List<SupportTicket>> getMyTickets(@RequestBody Authenticate authenticationResponse) {
        return supportTicketService.getMyTickets(authenticationResponse);
    }

    @RequestMapping("/getStats")
    public ResponseEntity<StatisticsDTO> getStats(@RequestBody AuthenticationResponse authenticationResponse) {

            return supportTicketService.getStatistics();


    }

    @RequestMapping("/getMyStats")
    public ResponseEntity<StatsByAgent> getMyStats(@RequestBody Authenticate authenticationResponse) {
        Optional<Admin> admin = Optional.ofNullable(adminRepository.findByEmail(authenticationResponse.getToken()));
        return supportTicketService.getStatsByAgent(admin.get());
    }


}
