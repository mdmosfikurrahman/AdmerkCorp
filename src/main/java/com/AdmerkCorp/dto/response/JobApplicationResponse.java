package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.JobApplication;
import lombok.Data;

@Data
public class JobApplicationResponse {
    private UserResponse user;
    private JobResponse job;
    private CoverLetter coverLetter;

    public JobApplicationResponse(JobApplication jobApplication) {
        this.user = new UserResponse(jobApplication.getUser());
        this.job = new JobResponse(jobApplication.getJob());
        this.coverLetter = jobApplication.getCoverLetter();
    }
}
