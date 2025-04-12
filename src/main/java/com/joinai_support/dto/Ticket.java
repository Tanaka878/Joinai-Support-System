package com.joinai_support.dto;

import lombok.Data;

@Data
public class Ticket {
    String name;
    long High;
    long Low;
    long Normal;
    long Urgent;
}
