package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.response.JobApplicationResponse;
import com.AdmerkCorp.dto.response.JobResponse;
import com.AdmerkCorp.dto.response.UserResponse;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.model.job.CoverLetter;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.service.JobApplicationService;
import com.AdmerkCorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JobApplicationService jobApplicationService;

    @GetMapping("/account")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserResponse> getAccountInfo(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(userResponse);
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


//    @PostMapping("/application/{jobId}")
//    @PreAuthorize("hasAuthority('user:job_apply')")
//    public ResponseEntity<String> applyJob(@PathVariable Long jobId, @RequestBody CoverLetter coverLetter, Principal principal) {
//        User user = userService.getUserByUsername(principal.getName());
//        try {
//            jobApplicationService.applyToJob(user, jobId, coverLetter);
//            return ResponseEntity.ok("Job applied successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to apply for the job");
//        }
//    }

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

}