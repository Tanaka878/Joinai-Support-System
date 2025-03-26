package com.joinai_support.controller;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.AuthenticationResponse;
import com.joinai_support.dto.StatisticsDTO;
import com.joinai_support.dto.TicketStatusDTO;
import com.joinai_support.repository.AdminRepository;
import com.joinai_support.service.SupportTicketService;
import com.joinai_support.springSecurity.config.JWTService;
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
    private final JWTService jwtService;
    private final AdminRepository adminRepository;
    private final UserValidator userValidator;


    @Autowired
    public SupportTicketController(SupportTicketService supportTicketService, JWTService jwtService, AdminRepository adminRepository, UserValidator userValidator) {
        this.supportTicketService = supportTicketService;
        this.jwtService = jwtService;
        this.adminRepository = adminRepository;
        this.userValidator = userValidator;
    }

    @PostMapping("/launchTicket")
    public String launchTicket(@RequestBody SupportTicket supportTicket) {
        return supportTicketService.launchTicket(supportTicket);
    }

    @RequestMapping("/updateTicket")
    public ResponseEntity<String> updateTicket(@RequestBody TicketStatusDTO supportTicket) {
        /*Admin admin = adminRepository.findByEmail(jwtService.extractUserName(supportTicket.getToken()));
        boolean isValid = jwtService.validateToken(supportTicket.getToken(), admin);
*/
        /*if (!isValid) {
            return ResponseEntity.badRequest().build();
        }*/
        System.out.println(" The user Id"+ supportTicket.getTicketId());
        return supportTicketService.updateTicket(supportTicket);
    }

    @GetMapping("/getMyTickets")
    public ResponseEntity<List<SupportTicket>> getMyTickets(@RequestBody AuthenticationResponse authenticationResponse) {
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


}
