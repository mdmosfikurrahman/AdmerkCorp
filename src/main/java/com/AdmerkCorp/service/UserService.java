package com.AdmerkCorp.service;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.Job;

import java.security.Principal;
import java.util.List;

public interface UserService {

    void validateUser(String usernameOrEmail);
    List<Job> getAllJobs();
    String changePassword(ChangePasswordRequest request, Principal connectedUser);
    List<User> getAllUsers();
    void deleteUserById(Long id);
    User getUserByUsername(String username);

}