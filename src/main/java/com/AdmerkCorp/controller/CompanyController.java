package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.ChangePasswordRequest;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.service.CompanyService;
import com.AdmerkCorp.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/company")
@PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final JobApplicationService jobApplicationService;

    @GetMapping("/account")
    @PreAuthorize("hasAnyAuthority('admin:read', 'company:read')")
    public ResponseEntity<Company> getAccountInfo(Principal principal) {
        Company company = companyService.getCompanyByUsername(principal.getName());
        return ResponseEntity.ok(company);
    }

    @PutMapping("/password")
    @PreAuthorize("hasAnyAuthority('admin:update', 'company:password_change')")
    public ResponseEntity<String> companyPasswordChange(@RequestBody ChangePasswordRequest request, Principal principal) {
        try {
            String result = companyService.changePassword(request, principal);
            return ResponseEntity.ok(result);
        } catch (AccessForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/application")
    @PreAuthorize("hasAnyAuthority('admin:read', 'company:read')")
    public List<JobApplication> getAllApplications() {
        return jobApplicationService.getAllApplications();
    }

    @PostMapping("/job")
    @PreAuthorize("hasAnyAuthority('admin:create', 'company:create')")
    public ResponseEntity<String> createJob(@RequestBody Job job, Principal principal) {
        Company company = companyService.getCompanyByUsername(principal.getName());
        Job createdJob = companyService.createJob(job, company);
        return ResponseEntity.status(HttpStatus.CREATED).body("Job created with ID: " + createdJob.getId());
    }

    @GetMapping("/job")
    @PreAuthorize("hasAnyAuthority('admin:read', 'company:read')")
    public ResponseEntity<List<Job>> getAllJobsByCompany(Principal principal) {
        Company company = companyService.getCompanyByUsername(principal.getName());
        List<Job> jobs = companyService.getAllJobsByCompany(company);
        return ResponseEntity.ok(jobs);
    }

}
