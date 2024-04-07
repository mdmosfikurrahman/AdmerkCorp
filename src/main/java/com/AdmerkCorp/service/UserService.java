package com.AdmerkCorp.service;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateUserProfileRequest;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.Job;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface UserService {

    void validateUser(String usernameOrEmail);
    List<Job> getAllJobs();
    String changePassword(ChangePasswordRequest request, Principal connectedUser);
    User getUserByUsername(String username);
    void updateUserProfile(Long userId, UpdateUserProfileRequest request) throws IOException;
    ByteArrayResource downloadCV(Long userId) throws IOException;

}