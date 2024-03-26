package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateUserProfileRequest;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.exception.ResourceAlreadyExistsException;
import com.AdmerkCorp.exception.ResourceNotFoundException;
import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.repository.UserRepository;
import com.AdmerkCorp.service.JobService;
import com.AdmerkCorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JobService jobService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void validateUser(String usernameOrEmail) {
        User existingUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (existingUser == null) {
            throw new ResourceAlreadyExistsException("User with this username already exists");
        }

    }

    @Override
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AccessForbiddenException("Wrong password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return "Password changed Successfully!";
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        } else {
            userRepository.deleteById(id);
        }

        userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    public void updateUserProfile(Long userId, UpdateUserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (user.isRefugee() && !request.getIsRefugee()) {
            user.setRefugee(false);
            user.setRefugeeNumber(null);
        } else {
            user.setRefugee(request.getIsRefugee());
            if (request.getIsRefugee() && user.getRefugeeNumber() == null) {
                user.setRefugeeNumber(generateRefugeeNumber(request));
            }
        }

        user.setEmail(request.getEmail());
        user.setBirthDate(request.getBirthDate());

        Location location = user.getLocation();
        location.setDivision(request.getLocation().getDivision());
        location.setCountry(request.getLocation().getCountry());
        location.setState(request.getLocation().getState());
        location.setCity(request.getLocation().getCity());
        location.setAddress(request.getLocation().getAddress());
        location.setZipCode(request.getLocation().getZipCode());

        userRepository.save(user);
    }

    private String generateRefugeeNumber(UpdateUserProfileRequest request) {
        String uuid = UUID.randomUUID().toString();
        String initials = String.valueOf(request.getFirstName().charAt(0)).toUpperCase() + String.valueOf(request.getLastName().charAt(0)).toUpperCase();
        return initials + "-" + uuid.substring(0, 6).toUpperCase();
    }

}
