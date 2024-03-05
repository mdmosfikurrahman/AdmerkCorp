package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.ChangePasswordRequest;
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

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JobApplicationService jobApplicationService;

    @GetMapping("/account")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<User> getAccountInfo(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/password")
    @PreAuthorize("hasAnyAuthority('admin:update', 'user:password_change')")
    public ResponseEntity<String> companyPasswordChange(@RequestBody ChangePasswordRequest request, Principal principal) {
        try {
            String result = userService.changePassword(request, principal);
            return ResponseEntity.ok(result);
        } catch (AccessForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/job")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read_jobs')")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> allJobs = userService.getAllJobs();
        return ResponseEntity.ok(allJobs);
    }

    @GetMapping("/application")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<List<JobApplication>> getAllAppliedJobs(Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        List<JobApplication> appliedJobs = jobApplicationService.getApplicationsByUser(user);
        return ResponseEntity.ok(appliedJobs);
    }

    @PostMapping("/application/{jobId}")
    @PreAuthorize("hasAnyAuthority('admin:create', 'user:job_apply')")
    public ResponseEntity<String> applyJob(@PathVariable Long jobId, @RequestBody CoverLetter coverLetter, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        try {
            jobApplicationService.applyToJob(user, jobId, coverLetter);
            return ResponseEntity.ok("Job applied successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to apply for the job");
        }
    }

}
