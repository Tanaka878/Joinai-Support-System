package com.joinai_support.service;

import com.joinai_support.domain.User;
import com.joinai_support.dto.GetResponse;
import com.joinai_support.repository.AdminRepository;
import com.joinai_support.domain.Admin;
import com.joinai_support.dto.UserDTO;
import com.joinai_support.repository.UserRepository;
import com.joinai_support.springSecurity.config.JWTService;
import com.joinai_support.utils.Role;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JWTService jwtService;

    @Autowired
    public AdminService(AdminRepository adminRepository, JWTService jwtService, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
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

    public ResponseEntity<List<Admin>> getAllAgents(GetResponse request) {
        Optional<User> user = userRepository.findByEmail(jwtService.extractUserName(request.getToken()));

        if (user.isPresent() && jwtService.validateToken(request.getToken(), user.get())) {
            List<Admin> allAdmins = adminRepository.findAll();
            return ResponseEntity.ok(allAdmins.stream().filter(admin -> admin.getRole() == Role.AGENT).toList());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public ResponseEntity<Admin> editProfile(GetResponse request) {
        Optional<User> user = userRepository.findByEmail(jwtService.extractUserName(request.getToken()));

        if (user.isPresent() && jwtService.validateToken(request.getToken(), user.get())) {
            Admin admin = adminRepository.findByEmail(request.getAdmin().getEmail());
            admin.setFirstName(request.getAdmin().getFirstName());
            admin.setRole(request.getAdmin().getRole());
            admin.setPassword(request.getAdmin().getPassword());
            adminRepository.save(admin);
            return ResponseEntity.ok(admin);

        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



        }
}
