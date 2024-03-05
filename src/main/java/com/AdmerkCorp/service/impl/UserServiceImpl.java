package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.exception.ResourceNotFoundException;
import com.AdmerkCorp.exception.ValidationException;
import com.AdmerkCorp.model.user.Role;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.repository.UserRepository;
import com.AdmerkCorp.service.JobApplicationService;
import com.AdmerkCorp.service.JobService;
import com.AdmerkCorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JobService jobService;
    private final JobApplicationService jobApplicationService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean validateUser(String usernameOrEmail) {
        User existingUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (existingUser == null) {
            throw new ValidationException("Invalid user username or email");
        }

        return true;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AccessForbiddenException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new AccessForbiddenException("Password doesn't match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return "Password changed Successfully!";
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> deleteUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        } else {
            userRepository.deleteById(id);
        }

        return userRepository.findAll();
    }

    @Override
    public List<User> deleteAllUsers() {
        if (userRepository.count() != 0) {
            userRepository.deleteAll();
        } else {
            throw new ResourceNotFoundException("No Users Found!");
        }
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

}
