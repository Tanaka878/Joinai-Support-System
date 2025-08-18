package com.joinai_support.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketDTO {
    private String id;
    private String category;
    private String status;
    private String subject;
    private String content;
    private String priority;
    private String[] attachments;
    private String launchTimestamp;
    private String updatedAt;
    private String assignedTo;
    private List<String> replies;
}
