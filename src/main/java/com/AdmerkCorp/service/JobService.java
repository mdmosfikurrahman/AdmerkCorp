package com.AdmerkCorp.service;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.*;

import java.util.List;

public interface JobService {

    Job createJob(Job job);

    List<Job> getAllJobs();

    Job getJobById(Long jobId);

    List<Job> getAllJobsByCompany(Company company);

    List<Job> getFilteredJobs(String companyName, JobType jobType, SalaryDuration salaryDuration, Category category, Experience experience);

    void deleteJobById(Long jobId);
}

