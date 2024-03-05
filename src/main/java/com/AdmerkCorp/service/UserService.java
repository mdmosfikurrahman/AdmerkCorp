package com.AdmerkCorp.service;

import com.AdmerkCorp.dto.ChangePasswordRequest;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    boolean validateUser(String usernameOrEmail);
    Optional<User> authenticateUser(String username, String password);
    List<Job> getAllJobs();
    JobApplication applyToJob(User user, Long jobId, CoverLetter coverLetter);
    List<JobApplication> getApplicationsByUser(User user);
    String changePassword(ChangePasswordRequest request, Principal connectedUser);
    User getUserById(Long id);
    List<User> getAllUsers();
    List<User> deleteUserById(Long id);
    List<User> deleteAllUsers();
    User getUserByUsername(String username);
}
