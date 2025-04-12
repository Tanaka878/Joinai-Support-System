package com.joinai_support.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OldTicketDTO {
    long id;
    LocalDateTime createdAt;
}
