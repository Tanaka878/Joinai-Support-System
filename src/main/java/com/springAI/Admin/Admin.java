package com.springAI.Admin;

import com.springAI.SupprtTicket.SupportTicket;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportTicket> tickets;


}
