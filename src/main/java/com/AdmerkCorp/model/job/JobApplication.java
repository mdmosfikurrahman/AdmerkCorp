package com.AdmerkCorp.model.job;

import com.AdmerkCorp.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @JoinColumn(name = "applied_on")
    private LocalDateTime appliedOn;

//    @Embedded
//    private CoverLetter coverLetter;

}