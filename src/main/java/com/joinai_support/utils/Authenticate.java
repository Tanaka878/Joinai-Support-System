package com.joinai_support.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Authenticate {
    private String token;
    private Role role;
    private String email;
}
