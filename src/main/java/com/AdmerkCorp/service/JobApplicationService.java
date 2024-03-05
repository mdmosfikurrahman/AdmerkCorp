package com.AdmerkCorp.service;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.JobApplication;

import java.util.List;

public interface JobApplicationService {

    void applyToJob(User user, Long jobId, CoverLetter coverLetter);
    List<JobApplication> getApplicationsByUser(User user);
    List<JobApplication> getAllApplications();
    void deleteApplicationById(Long applicationId);
    List<JobApplication> getApplicationsByCompany(Company company);

}