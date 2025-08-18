package com.joinai_support.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public TicketAnalysis(String ticketId, String question, String issuerEmail) {
        this.issuerEmail= issuerEmail;
        this.ticketId = ticketId;
        this.question = question;
    }

    public void addReply(String reply) {
        this.replies.add(reply);
    }
}
