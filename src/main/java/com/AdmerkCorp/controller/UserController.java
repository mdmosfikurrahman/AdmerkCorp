package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateUserProfileRequest;
import com.AdmerkCorp.dto.response.JobApplicationResponse;
import com.AdmerkCorp.dto.response.JobResponse;
import com.AdmerkCorp.dto.response.LocationResponse;
import com.AdmerkCorp.dto.response.UserResponse;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.service.JobApplicationService;
import com.AdmerkCorp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserController {

    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final JobApplicationService jobApplicationService;

    @GetMapping("/account")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserResponse> getAccountInfo(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/account/{userId}")
    @PreAuthorize("hasAuthority('user:update_profile')")
    public ResponseEntity<String> updateUserProfile(
            @PathVariable Long userId,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestParam(value = "curriculumVitae", required = false) MultipartFile curriculumVitae,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "isRefugee", required = false) Boolean isRefugee,
            @RequestParam(value = "birthDate", required = false) String birthDate,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "contactNumber", required = false) String contactNumber) {

        try {
            LocalDate parsedBirthDate = birthDate != null ? LocalDate.parse(birthDate) : null;
            LocationResponse locationResponse = location != null ? objectMapper.readValue(location, LocationResponse.class) : null;

            UpdateUserProfileRequest request = new UpdateUserProfileRequest(
                    firstName, lastName, isRefugee, parsedBirthDate, email, locationResponse, contactNumber, profilePicture, curriculumVitae);
            userService.updateUserProfile(userId, request);
            return ResponseEntity.ok("User profile updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user profile");
        }
    }

    @PutMapping("/password")
    @PreAuthorize("hasAuthority('user:password_change')")
    public ResponseEntity<String> companyPasswordChange(@RequestBody ChangePasswordRequest request, Principal principal) {
        try {
            String result = userService.changePassword(request, principal);
            return ResponseEntity.ok(result);
        } catch (AccessForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/job")
    @PreAuthorize("hasAuthority('user:read_jobs')")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<Job> allJobs = userService.getAllJobs();
        List<JobResponse> jobResponseDTOs = allJobs.stream()
                .map(JobResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(jobResponseDTOs);
    }

    @GetMapping("/application")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<List<JobApplicationResponse>> getAllAppliedJobs(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<JobApplication> appliedJobs = jobApplicationService.getApplicationsByUser(user);
        List<JobApplicationResponse> applicationResponseDTOs = appliedJobs.stream()
                .map(JobApplicationResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(applicationResponseDTOs);
    }

    @PostMapping("/application/{jobId}")
    @PreAuthorize("hasAuthority('user:job_apply')")
    public ResponseEntity<String> applyJob(@PathVariable Long jobId, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        try {
            jobApplicationService.applyToJob(user, jobId);
            return ResponseEntity.ok("Job applied successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to apply for the job");
        }
    }

    @GetMapping("/download-cv/{userId}")
    @PreAuthorize("hasAuthority('user:cv_download')")
    public ResponseEntity<ByteArrayResource> downloadCV(@PathVariable Long userId) {
        return userService.downloadCV(userId);
    }

}