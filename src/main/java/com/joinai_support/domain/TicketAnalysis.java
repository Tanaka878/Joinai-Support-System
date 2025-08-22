package com.joinai_support.domain;

import com.joinai_support.utils.Category;
import com.joinai_support.utils.Priority;
import com.joinai_support.utils.Status;
import com.joinai_support.utils.TicketSource;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "ticket_analysis")
@Data
public class TicketAnalysis {

    @Id
    private String ticketId;
    private String question;
    private String issuerEmail;
    private List<String> replies = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private Long resolutionTimeMinutes;

    private Status status;
    private Priority priority;
    private Category category;

    private TicketSource source;

    private Integer totalReplies;
    private LocalDateTime lastReplyAt;
    private Boolean requiresFollowup;


    public void addReply(String reply) {
        this.replies.add(reply);
    }
}
