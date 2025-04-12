package com.joinai_support.dto;

import lombok.Data;

import java.util.List;

@Data
public class SystemAnalytics {

    private long openTickets;
    private long totalAgents;
    private long dailyTickets;
    private List<PerformanceDTO> performance;
    List<Ticket> tickets;






}
