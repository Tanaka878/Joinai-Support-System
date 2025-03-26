package com.joinai_support.utils;


import com.joinai_support.domain.Admin;
import com.joinai_support.service.AdminService;
import com.joinai_support.springSecurity.config.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidator {

    private final JWTService jwtService;
    private final AdminService adminService;

    @Autowired
    public UserValidator(JWTService jwtService, AdminService adminService) {
        this.jwtService = jwtService;
        this.adminService = adminService;
    }

    public boolean Validator(String token) {
       String email = jwtService.extractUserName(token);
       Admin admin = adminService.getAdmin(email);
       return jwtService.validateToken(token, admin);
    }

}
