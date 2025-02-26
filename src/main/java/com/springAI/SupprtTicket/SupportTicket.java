package com.springAI.SupprtTicket;

import com.springAI.Admin.Admin;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private LocalDateTime launchTimestamp;
    private LocalDateTime servedTimestamp;
    private String ticketCandidate;
    private Duration timeLimit;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true) // Specifies the foreign key column
    private Admin assignedTo;

}
