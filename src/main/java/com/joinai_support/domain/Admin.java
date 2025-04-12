package com.joinai_support.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Admin extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phone;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private LocalDateTime lastLogin;

    @OneToMany(mappedBy = "assignedTo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SupportTicket> tickets;
}
