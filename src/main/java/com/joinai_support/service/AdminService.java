package com.joinai_support.service;

import com.joinai_support.domain.SupportTicket;
import com.joinai_support.domain.User;
import com.joinai_support.dto.GetResponse;
import com.joinai_support.dto.PerformanceDTO;
import com.joinai_support.dto.SystemAnalytics;
import com.joinai_support.repository.AdminRepository;
import com.joinai_support.domain.Admin;
import com.joinai_support.dto.UserDTO;
import com.joinai_support.repository.SupportTicketRepository;
import com.joinai_support.repository.UserRepository;
import com.joinai_support.utils.AdminDTO;
import com.joinai_support.utils.Role;
import com.joinai_support.utils.Status;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final SupportTicketRepository supportTicketRepository;


    @Autowired
    public AdminService(AdminRepository adminRepository, UserRepository userRepository, SupportTicketRepository supportTicketRepository) {
        this.adminRepository = adminRepository;

        this.userRepository = userRepository;
        this.supportTicketRepository = supportTicketRepository;
    }

    @Transactional
    public ResponseEntity<String> createAgent(UserDTO admin) {
        Admin agent = new Admin();
        agent.setEmail(admin.getEmail());
        agent.setPassword(admin.getPassword());
        agent.setFirstName(admin.getFirstName());
        agent.setRole(Role.AGENT);
        adminRepository.save(agent);
        return ResponseEntity.ok("Agent created");
    }


    @Transactional
    public ResponseEntity<String> createAdmin(UserDTO admin) {
        Admin admin1 = new Admin();
        admin1.setEmail(admin.getEmail());
        admin1.setPassword(admin.getPassword());
        admin1.setFirstName(admin.getFirstName());
        admin1.setRole(Role.ADMIN);
        adminRepository.save(admin1);
         return ResponseEntity.ok("Admin created");
    }

    public ResponseEntity<List<Admin>> getAll() {
        List<Admin> adminList = adminRepository.findAllByRole(Role.AGENT);
        return ResponseEntity.ok(adminList);
    }

    public Admin getAdmin(String email) {
        return adminRepository.findByEmail(email);
    }

    public ResponseEntity<List<Admin>> getAllAgents(GetResponse request) {
        Optional<User> user = userRepository.findByEmail(request.getToken());

        if (user.isPresent()) {
            List<Admin> allAdmins = adminRepository.findAll();
            return ResponseEntity.ok(allAdmins.stream().filter(admin -> admin.getRole() == Role.AGENT).toList());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Transactional
    public ResponseEntity<Admin> editProfile(AdminDTO request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent()) {
            Admin admin = adminRepository.findByEmail(request.getEmail());

            // Check if each field is not empty before setting
            if (request.getName() != null && !request.getName().isEmpty()) {
                admin.setFirstName(request.getName());
            }

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                admin.setPassword(request.getPassword());
            }

            if (request.getCity() != null && !request.getCity().isEmpty()) {
                admin.setCity(request.getCity());
            }

            if (request.getCountry() != null && !request.getCountry().isEmpty()) {
                admin.setCountry(request.getCountry());
            }

            if (request.getPhone() != null && !request.getPhone().isEmpty()) {
                admin.setPhone(request.getPhone());
            }

            if (request.getAddress() != null && !request.getAddress().isEmpty()) {
                admin.setAddress(request.getAddress());
            }

            if (request.getZip() != null && !request.getZip().isEmpty()) {
                admin.setZip(request.getZip());
            }

            // Save the updated admin profile
            adminRepository.save(admin);

            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @Transactional
    public ResponseEntity<Admin> deleteProfile(GetResponse request) {

        Optional<User> user = userRepository.findByEmail(request.getToken());

        if (user.isPresent()) {
            Admin admin = adminRepository.findByEmail(request.getAdmin().getEmail());
            adminRepository.delete(admin);
        return ResponseEntity.ok(admin);
        }
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Transactional
    public void TrackActivity(Admin agent) {
       Admin user = adminRepository.findByEmail(agent.getEmail());
       if (user != null) {
           user.setLastLogin(LocalDateTime.now());
       }

    }

    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(supportTicketRepository.findAll());
    }
    
    public ResponseEntity<SystemAnalytics> systemAnalytics() {
        // total  agents
        List<Admin> allAdmins = adminRepository.findAll();
        long agents = allAdmins.stream().filter(admin -> admin.getRole() == Role.AGENT).toList().size();
        
        //open tickets
        long openTickets=supportTicketRepository.findAll().parallelStream()
                .filter(supportTicket -> supportTicket.getStatus() == Status.OPEN).count();
        
        //daily tickets  

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime =LocalDateTime.now();
        List<SupportTicket> tickets = supportTicketRepository.findAll();

        // Daily stats (last 24 hours)
        LocalDateTime dailyStart = now.minusHours(24);
        long dailytickets = (tickets.stream()
                .filter(supportTicket ->
                        supportTicket.getLaunchTimestamp().isAfter(dailyStart) ||
                                supportTicket.getLaunchTimestamp().isEqual(dailyStart))
                .count());



        PerformanceDTO performance = new PerformanceDTO();

        adminRepository.findAllByRole(Role.AGENT).forEach(admin -> {
            performance.setFrc(34.2);
            performance.setOpenTickets(admin.getTickets()
                    .parallelStream().filter(adminTicket -> adminTicket.getStatus() == Status.OPEN).count());

        });

        SystemAnalytics systemAnalytics = new SystemAnalytics();
        systemAnalytics.setTotalAgents(agents);
        systemAnalytics.setOpenTickets(openTickets);
        systemAnalytics.setDailyTickets(dailytickets);

        
        return ResponseEntity.ok(systemAnalytics);
    }
}
