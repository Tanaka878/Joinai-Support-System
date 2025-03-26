package com.joinai_support.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import com.joinai_support.utils.Category;
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

    private String content;
    private String attachments;
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private Duration timeLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true)
    @JsonBackReference
    private Admin assignedTo;

    @PrePersist
    public void prePersist() {
        // Set launchTimestamp to the current time before persisting
        if (launchTimestamp == null) {
            launchTimestamp = LocalDateTime.now();
        }

        // Set updatedAt to the current time before persisting
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }

        // Set default status if not set (optional, can be customized)
        if (status == null) {
            status = Status.NEW;  // Assuming "NEW" is the default status
        }
    }
}
