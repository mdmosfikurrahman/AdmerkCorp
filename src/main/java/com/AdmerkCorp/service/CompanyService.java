package com.AdmerkCorp.service;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;

import java.util.List;

public interface CompanyService {

    Company registerCompany(Company company);

    Job createJob(Job job, Company company);

    List<JobApplication> getJobApplicants(Long jobId);
    Company authenticateCompany(String companyName, String password);
}
