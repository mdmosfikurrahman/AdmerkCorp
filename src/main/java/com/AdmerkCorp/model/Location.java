package com.AdmerkCorp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String state;
    private String division;
    private String city;
    private String address;
    private String zipCode;

}
