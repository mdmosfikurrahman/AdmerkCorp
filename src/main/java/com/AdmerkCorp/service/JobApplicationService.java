package com.AdmerkCorp.service;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.JobApplicationStatus;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.JobApplication;

import java.util.List;

public interface JobApplicationService {

    void applyToJob(User user, Long jobId);
    List<JobApplication> getApplicationsByUser(User user);
    List<JobApplication> getAllApplications();
    void deleteApplicationById(Long applicationId);
    List<JobApplication> getApplicationsByCompany(Company company);
    JobApplication getApplicationsByCompanyAndUserId(Company company, Long userId);
    void respondToApplication(Company company, Long userId, JobApplicationStatus response);

}