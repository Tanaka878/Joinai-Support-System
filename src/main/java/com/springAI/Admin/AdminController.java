package com.springAI.Admin;

import com.springAI.DTO.AdminLoginRequest;
import com.springAI.DTO.AuthenticationResponse;
import com.springAI.DTO.UserDTO;
import com.springAI.SupprtTicket.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AdminController(AdminService adminService, AuthenticationService authenticationService) {
        this.adminService = adminService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<String> createAdmin(@RequestBody UserDTO admin) {
        return adminService.createAdmin(admin);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return null;
    }

    @PostMapping("/authenticate/")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AdminLoginRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(response); // Return a 200 OK response with the token
    }

}
