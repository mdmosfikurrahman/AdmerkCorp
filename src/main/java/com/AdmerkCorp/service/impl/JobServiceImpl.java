package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.repository.JobApplicationRepository;
import com.AdmerkCorp.repository.JobRepository;
import com.AdmerkCorp.service.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + jobId));
    }

    public List<JobApplication> getJobApplicants(Long jobId) {
        Job job = getJobById(jobId);
        return jobApplicationRepository.findByJob(job);
    }

    @Override
    public List<Job> getAllJobsByCompany(Company company) {
        return jobRepository.findByCompany(company);
    }
}
