package com.springAI.DTO;


import com.springAI.User.Gender;
import com.springAI.User.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
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


    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", firstName='" + firstName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", gender=" + gender +
                '}';
    }
}
