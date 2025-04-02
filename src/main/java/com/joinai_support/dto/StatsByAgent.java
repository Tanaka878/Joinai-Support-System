package com.joinai_support.dto;

import lombok.Data;

@Data
public class StatsByAgent {
    //the data should show the tickets assigned against the tickets solved
    private long SOLVED_DAILY;
    private long SOLVED_WEEKLY;
    private long SOLVED_MONTHLY;
    private long DAILY_TICKETS;
    private long WEEKLY_TICKETS;
    private long MONTHLY_TICKETS;
}
