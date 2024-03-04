package com.AdmerkCorp.service;

import com.AdmerkCorp.model.User;
import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;

import java.util.List;

public interface UserService {
    User registerUser(User user);
    boolean validateUser(String usernameOrEmail);
    User authenticateUser(String username, String password);
    List<Job> getAllJobs();

    JobApplication applyToJob(User user, Long jobId, CoverLetter coverLetter);

    List<JobApplication> getApplicationsByUser(User user);
}