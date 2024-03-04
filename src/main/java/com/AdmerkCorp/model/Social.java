package com.AdmerkCorp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Social {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String facebook;
    private String linkedIn;
    private String twitter;
    private String instagram;
    private String whatsApp;

}
