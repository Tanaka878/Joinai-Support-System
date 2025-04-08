package com.joinai_support.controller;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.AdminLoginRequest;
import com.joinai_support.dto.AuthenticationResponse;
import com.joinai_support.dto.GetResponse;
import com.joinai_support.dto.UserDTO;
import com.joinai_support.repository.SupportTicketRepository;
import com.joinai_support.service.AdminService;
import com.joinai_support.service.AuthenticationService;

import com.joinai_support.springSecurity.config.JWTService;
import com.joinai_support.springSecurity.config.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;
    private final AuthenticationService authenticationService;
    private final SupportTicketRepository supportTicketRepository;
    private final JWTService jwtService;

    @Autowired
    public AdminController(AdminService adminService, AuthenticationService authenticationService, SupportTicketRepository supportTicketRepository, JwtAuthenticationFilter jwtAuthenticationFilter, JWTService jwtService) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
        this.supportTicketRepository = supportTicketRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<String> createAdmin(@RequestBody UserDTO admin) {
        return adminService.createAdmin(admin);
    }

    @PostMapping("/createAgent")
    public ResponseEntity<String> createAgent(@RequestBody UserDTO admin) {
        return adminService.createAgent(admin);
    }

    @PostMapping("/authenticate/")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AdminLoginRequest authenticationRequest) {
        System.out.println("Authentication");
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        String token = response.getToken();
        Optional<Admin> user = Optional.ofNullable(adminService.getAdmin(jwtService.extractUserName(token)));
        user.ifPresent(admin -> response.setRole(admin.getRole()));
        user.ifPresent(admin -> response.setId(user.get().getId()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getAgents")
    public ResponseEntity<List<Admin>> getAgents(@RequestBody GetResponse request) {
        return adminService.getAllAgents(request);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        List<SupportTicket> users = supportTicketRepository.findAll();
       return ResponseEntity.ok(users);
    }

    @PostMapping("/editProfile")
    public ResponseEntity<Admin> editProfile(@RequestBody GetResponse request) {
        return adminService.editProfile(request);
    }

    @PostMapping("/deleteProfile")
    public ResponseEntity<Admin> deleteAgentProfile(@RequestBody GetResponse request) {
        return adminService.deleteProfile(request);

    }
}
