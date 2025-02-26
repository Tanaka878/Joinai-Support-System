package com.springAI.SupprtTicket;

import com.springAI.Admin.Admin;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private LocalDateTime launch_timestamp;
    private LocalDateTime served_timestamp;
    private String ticketCandidate;
    private Duration time_limit;

    @OneToMany(mappedBy = Admin)
    private Admin assignedTo;


}
