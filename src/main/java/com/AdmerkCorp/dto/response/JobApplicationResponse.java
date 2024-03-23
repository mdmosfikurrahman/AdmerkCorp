package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.JobApplication;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobApplicationResponse {

    @JsonProperty("user_info")
    private UserResponse user;
    @JsonProperty("job_info")
    private JobResponse job;
    @JsonProperty("application_id")
    private Long applicationId;
    @JsonProperty("applied_on")
    private LocalDateTime appliedOn;
//    private CoverLetter coverLetter;

    public JobApplicationResponse(JobApplication jobApplication) {
        this.user = new UserResponse(jobApplication.getUser());
        this.job = new JobResponse(jobApplication.getJob());
        this.applicationId = jobApplication.getId();
        this.appliedOn = jobApplication.getAppliedOn();
//        this.coverLetter = jobApplication.getCoverLetter();
    }

}