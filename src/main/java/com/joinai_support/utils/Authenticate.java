package com.joinai_support.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Authenticate {
    String token;
    Role role;
}
