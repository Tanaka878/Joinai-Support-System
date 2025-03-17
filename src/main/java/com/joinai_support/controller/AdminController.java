package com.joinai_support.controller;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.AdminLoginRequest;
import com.joinai_support.dto.AuthenticationResponse;
import com.joinai_support.dto.UserDTO;
import com.joinai_support.repository.SupportTicketRepository;
import com.joinai_support.service.AdminService;
import com.joinai_support.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;
    private final AuthenticationService authenticationService;
    private final SupportTicketRepository supportTicketRepository;

    @Autowired
    public AdminController(AdminService adminService, AuthenticationService authenticationService, SupportTicketRepository supportTicketRepository) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
        this.supportTicketRepository = supportTicketRepository;
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<String> createAdmin(@RequestBody UserDTO admin) {
        return adminService.createAdmin(admin);
    }


    @PostMapping("/authenticate/")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AdminLoginRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(response); // Return a 200 OK response with the token
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SupportTicket>> getAllUsers() {
        List<SupportTicket> users = supportTicketRepository.findAll();
       return ResponseEntity.ok(users);
    }


}
