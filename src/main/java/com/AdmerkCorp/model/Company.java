package com.AdmerkCorp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String companyName;
    private String companyMail;
    private String password;
    private String website;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "social_id")
    private Social social;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    private Location location;

}
