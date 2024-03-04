package com.AdmerkCorp.service;

import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;

import java.util.List;

public interface JobService {

    Job createJob(Job job);

    List<Job> getAllJobs();

    Job getJobById(Long jobId);

    List<JobApplication> getJobApplicants(Long jobId);
}

