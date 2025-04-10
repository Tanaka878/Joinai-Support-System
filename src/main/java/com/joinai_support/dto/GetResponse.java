package com.joinai_support.dto;

import com.joinai_support.domain.Admin;
import lombok.Data;

@Data
public class GetResponse {
    private String token;
    private Admin admin;
}
