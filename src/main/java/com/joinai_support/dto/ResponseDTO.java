package com.joinai_support.dto;

import com.joinai_support.utils.Role;
import lombok.Data;

@Data
public class ResponseDTO {
    public String token;
    public Role role;
    public Long id;

}
