package com.joinai_support.dto;

import lombok.Data;

import java.util.List;

@Data
public class PerformanceDTO {
    //for agent related stats
    String agentName;
    long openTickets;
    List<OldTicketDTO> oldTickets;
    double frc;

}
