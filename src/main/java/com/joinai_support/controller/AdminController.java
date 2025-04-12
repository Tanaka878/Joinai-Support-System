package com.joinai_support.controller;

import com.joinai_support.domain.Admin;
import com.joinai_support.domain.SupportTicket;
import com.joinai_support.dto.*;
import com.joinai_support.repository.SupportTicketRepository;
import com.joinai_support.service.AdminService;


import com.joinai_support.utils.AdminDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    private final SupportTicketRepository supportTicketRepository;

    @Autowired
    public AdminController(AdminService adminService,  SupportTicketRepository supportTicketRepository) {
        this.adminService = adminService;

        this.supportTicketRepository = supportTicketRepository;
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
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody AdminLoginRequest authenticationRequest) {
        ResponseDTO response = new ResponseDTO();

        // Fetch admin by email
        Admin user = adminService.getAdmin(authenticationRequest.getEmail());
        if (user == null) {
            // Return unauthorized if user is not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();}

        // Validate password (assuming plain text, consider using hashing)
        if (!user.getPassword().equals(authenticationRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        adminService.TrackActivity(user);
        response.setRole(user.getRole());
        response.setId(user.getId());

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

    @PostMapping("/updateProfile")
    public ResponseEntity<Admin> editProfile(@RequestBody AdminDTO request) {
        return adminService.editProfile(request);
    }


    @PostMapping("/deleteProfile")
    public ResponseEntity<Admin> deleteAgentProfile(@RequestBody GetResponse request) {
        return adminService.deleteProfile(request);
    }

    @PostMapping("/getAllTickets")
    public ResponseEntity<List<SupportTicket>> getAllTickets(@RequestBody EmailRequest request) {
        Admin admin = adminService.getAdmin(request.getEmail());
        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else return adminService.getAllTickets();

    }

    @PostMapping("/getAnalytics")
    public ResponseEntity<SystemAnalytics>  getAnalytics(@RequestBody GetResponse request) {
        return adminService.systemAnalytics();
    }
}
