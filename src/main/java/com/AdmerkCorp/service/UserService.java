package com.AdmerkCorp.service;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean validateUser(String usernameOrEmail);
    List<Job> getAllJobs();
    String changePassword(ChangePasswordRequest request, Principal connectedUser);
    User getUserById(Long id);
    List<User> getAllUsers();
    List<User> deleteUserById(Long id);
    List<User> deleteAllUsers();
    User getUserByUsername(String username);
}
