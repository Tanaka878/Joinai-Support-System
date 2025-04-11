package com.joinai_support.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {

    private Long id;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime launchTimestamp;
    
    private String subject;
    
    @Enumerated(EnumType.STRING)
    private Priority priority;
    
    private String content;
    
    private String attachments;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;
    
    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Category category;


}
