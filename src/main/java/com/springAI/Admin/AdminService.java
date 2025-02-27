package com.springAI.Admin;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    @Transactional
    public ResponseEntity<String> createAdmin(Admin admin) {
         adminRepository.save(admin);
         return ResponseEntity.ok("Admin created");
    }
}
