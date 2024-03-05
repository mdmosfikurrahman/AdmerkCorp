package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.repository.JobApplicationRepository;
import com.AdmerkCorp.service.JobApplicationService;
import com.AdmerkCorp.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final JobService jobService;

    public JobApplication applyToJob(User user, Long jobId, CoverLetter coverLetter) {
        Job job = jobService.getJobById(jobId);
        JobApplication jobApplication = new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJob(job);
        jobApplication.setCoverLetter(coverLetter);
        return jobApplicationRepository.save(jobApplication);
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
}

