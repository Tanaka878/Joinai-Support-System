package com.joinai_support.service;

import com.joinai_support.repository.AdminRepository;
import com.joinai_support.domain.Admin;
import com.joinai_support.dto.UserDTO;
import com.joinai_support.utils.Role;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.loadtime.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class AdminService {

    private final AdminRepository adminRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


    @Transactional
    public ResponseEntity<String> createAdmin(UserDTO admin) {
        Admin admin1 = new Admin();
        admin1.setEmail(admin.getEmail());
        admin1.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin1.setFirstName(admin.getFirstName());
        admin1.setRole(admin.getRole());
        adminRepository.save(admin1);
         return ResponseEntity.ok("Admin created");
    }

    public ResponseEntity<List<Admin>> getAll() {
        List<Admin> adminList = adminRepository.findAll();
        return ResponseEntity.ok(adminList);
    }

    public Admin getAdmin(String email) {
        return adminRepository.findByEmail(email);
    }

    public ResponseEntity<List<Admin>> getAllAgents() {
        List<Admin> allAdmins = adminRepository.findAll();
        return ResponseEntity.ok(allAdmins.stream().filter(admin -> admin.getRole() == Role.AGENT).toList());

    }
}
