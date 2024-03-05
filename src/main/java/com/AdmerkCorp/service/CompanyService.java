package com.AdmerkCorp.service;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    Company registerCompany(Company company);

    Job createJob(Job job, Company company);

    List<JobApplication> getJobApplicants(Long jobId);
    Optional<Company> authenticateCompany(String companyName, String password);
}
