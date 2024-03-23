package com.AdmerkCorp.model.job;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.Location;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobTitle;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private int salaryAmount;

    @Enumerated(EnumType.STRING)
    private SalaryDuration salaryDuration;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;

    private String overview;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Enumerated(EnumType.STRING)
    private Experience experience;

    private String responsibility;

    private String requiredSkills;

    private String benefits;

    private String jobUrl;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    private List<JobApplication> applications;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}