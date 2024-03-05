package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.*;
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

    @Override
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with id: " + jobId));
    }

    @Override
    public List<Job> getAllJobsByCompany(Company company) {
        return jobRepository.findByCompany(company);
    }

    @Override
    public List<Job> getFilteredJobs(String companyName, JobType jobType, SalaryDuration salaryDuration, Category category, Experience experience) {
        return jobRepository.getFilteredJobs(companyName, jobType, salaryDuration, category, experience);
    }
}
