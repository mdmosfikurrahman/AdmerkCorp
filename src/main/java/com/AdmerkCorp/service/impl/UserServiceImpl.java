package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.exception.ValidationException;
import com.AdmerkCorp.model.Role;
import com.AdmerkCorp.model.User;
import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.repository.UserRepository;
import com.AdmerkCorp.service.JobApplicationService;
import com.AdmerkCorp.service.JobService;
import com.AdmerkCorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JobService jobService;
    private final JobApplicationService jobApplicationService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }

    @Override
    public boolean validateUser(String usernameOrEmail) {
        User existingUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (existingUser == null) {
            throw new ValidationException("Invalid user username or email");
        }

        return true;
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @Override
    public JobApplication applyToJob(User user, Long jobId, CoverLetter coverLetter) {
        return jobApplicationService.applyToJob(user, jobId, coverLetter);
    }

    @Override
    public List<JobApplication> getApplicationsByUser(User user) {
        return jobApplicationService.getApplicationsByUser(user);
    }

}
