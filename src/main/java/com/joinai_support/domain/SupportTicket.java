package com.joinai_support.domain;

import com.joinai_support.utils.Category;
import com.joinai_support.utils.Priority;
import com.joinai_support.utils.Status;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime launchTimestamp;
    private LocalDateTime servedTimestamp;
    private String subject;
    private Priority priority;
    private String content;
    private LocalDateTime updatedAt;
    private Status status;
    private Duration timeLimit;
    private Category category;


    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true) // Specifies the foreign key column
    private Admin assignedTo;

    public SupportTicket(Long id, LocalDateTime launchTimestamp,
                         LocalDateTime servedTimestamp, String subject,
                         Priority priority, String content, LocalDateTime updatedAt,
                         Status status, Duration timeLimit, Category category, Admin assignedTo) {
        this.id = id;
        this.launchTimestamp = launchTimestamp;
        this.servedTimestamp = servedTimestamp;
        this.subject = subject;
        this.priority = priority;
        this.content = content;
        this.updatedAt = updatedAt;
        this.status = status;
        this.timeLimit = timeLimit;
        this.category = category;
        this.assignedTo = assignedTo;
    }

    public SupportTicket() {
    }

    public SupportTicket(LocalDateTime launchTimestamp, LocalDateTime servedTimestamp,
                         String subject, Priority priority, String content, LocalDateTime updatedAt,
                         Status status, Duration timeLimit, Category category, Admin assignedTo) {
        this.launchTimestamp = launchTimestamp;
        this.servedTimestamp = servedTimestamp;
        this.subject = subject;
        this.priority = priority;
        this.content = content;
        this.updatedAt = updatedAt;
        this.status = status;
        this.timeLimit = timeLimit;
        this.category = category;
        this.assignedTo = assignedTo;
    }
}
