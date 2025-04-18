package com.joinai_support.dto;

import com.joinai_support.utils.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    public String token;
   public Role role;
    public Long id;
}
