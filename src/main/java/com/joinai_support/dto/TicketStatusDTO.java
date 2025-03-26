package com.joinai_support.dto;

import com.joinai_support.utils.Status;
import lombok.Data;

@Data
public class TicketStatusDTO {
    private Long ticketId;
    private Status status;
    private String token;
}
