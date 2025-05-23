package com.joinai_support.dto;


import com.joinai_support.utils.Gender;
import com.joinai_support.utils.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String firstName;
    private String username;
    private String password;
    private String email;
    private Role role;
    private Gender gender;
}
