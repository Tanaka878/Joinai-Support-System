package com.joinai_support.controller;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.AuthenticationResponse;
import com.joinai_support.dto.StatisticsDTO;
import com.joinai_support.dto.StatsByAgent;
import com.joinai_support.dto.TicketStatusDTO;
import com.joinai_support.service.SupportTicketService;
import com.joinai_support.utils.Authenticate;
import com.joinai_support.utils.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@CrossOrigin("*")
public class SupportTicketController {

    private final SupportTicketService supportTicketService;
    private final UserValidator userValidator;

    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService, UserValidator userValidator) {
        this.supportTicketService = supportTicketService;
        this.userValidator = userValidator;
    }

    @PostMapping("/launchTicket")
    public String launchTicket(@RequestBody SupportTicket supportTicket) {
        return supportTicketService.launchTicket(supportTicket);
    }

    @RequestMapping("/updateTicket")
    public ResponseEntity<String> updateTicket(@RequestBody TicketStatusDTO supportTicket) {
        if (!userValidator.Validator(supportTicket.getToken())) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println(" The user Id"+ supportTicket.getTicketId());
        return supportTicketService.updateTicket(supportTicket);
    }

    @GetMapping("/getMyTickets")
    public ResponseEntity<List<SupportTicket>> getMyTickets(@RequestBody Authenticate authenticationResponse) {
        return supportTicketService.getMyTickets(authenticationResponse);
    }

    @RequestMapping("/getStats")
    public ResponseEntity<StatisticsDTO> getStats(@RequestBody AuthenticationResponse authenticationResponse) {
        if (userValidator.Validator(authenticationResponse.getToken())){
            return supportTicketService.getStatistics();
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @RequestMapping("/getMyStats")
    public ResponseEntity<StatsByAgent> getMyStats(@RequestBody Authenticate authenticationResponse) {
        Admin admin =userValidator.validateAndGetAdmin(authenticationResponse.getToken());
        return supportTicketService.getStatsByAgent(admin);
    }


}
