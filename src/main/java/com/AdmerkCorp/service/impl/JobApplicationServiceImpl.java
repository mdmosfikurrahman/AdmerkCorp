package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.JobApplicationStatus;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.repository.JobApplicationRepository;
import com.AdmerkCorp.service.JobApplicationService;
import com.AdmerkCorp.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobService jobService;

    public void applyToJob(User user, Long jobId) {
        Job job = jobService.getJobById(jobId);
        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJob(job);
        jobApplication.setAppliedOn(LocalDateTime.now());
        jobApplication.setStatus(JobApplicationStatus.PENDING);
        jobApplicationRepository.save(jobApplication);
    }

    public List<JobApplication> getApplicationsByUser(User user) {
        return jobApplicationRepository.findByUser(user);
    }

    @Override
    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll();
    }

    @Override
    public void deleteApplicationById(Long applicationId) {
        jobApplicationRepository.deleteById(applicationId);
    }

    @Override
    public List<JobApplication> getApplicationsByCompany(Company company) {
        return jobApplicationRepository.findByJobCompany(company);
    }

    @Override
    public List<JobApplication> getApplicationsByCompanyAndUserId(Company company, Long userId) {
        List<JobApplication> jobApplicationList = jobApplicationRepository.findByJobCompany(company);
        List<JobApplication> userApplications = new ArrayList<>();

        for (JobApplication jobApplication : jobApplicationList) {
            if (jobApplication.getUser().getId().equals(userId)) {
                userApplications.add(jobApplication);
            }
        }

        return userApplications;
    }

    @Override
    public void respondToApplication(Company company, Long userId, JobApplicationStatus response) {
        JobApplication jobApplication = jobApplicationRepository.findByUserIdAndCompanyId(userId, company.getId());
        if (jobApplication != null) {
            jobApplication.setStatus(response);
            jobApplicationRepository.save(jobApplication);
        } else {
            throw new IllegalArgumentException("Job application not found for userId: " + userId + " and companyId: " + company.getId());
        }
    }
}