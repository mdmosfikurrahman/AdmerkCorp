package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateCompanyProfileRequest;
import com.AdmerkCorp.dto.response.CompanyResponse;
import com.AdmerkCorp.dto.response.JobApplicationResponse;
import com.AdmerkCorp.dto.response.JobResponse;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.model.job.JobApplicationStatus;
import com.AdmerkCorp.service.CompanyService;
import com.AdmerkCorp.service.JobApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
@PreAuthorize("hasRole('COMPANY')")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final JobApplicationService jobApplicationService;

    @GetMapping("/account")
    @PreAuthorize("hasAuthority('company:read')")
    public ResponseEntity<CompanyResponse> getAccountInfo(Principal principal) {
        Company company = companyService.getCompanyByUsername(principal.getName());
        CompanyResponse responseDTO = new CompanyResponse(company);
        return ResponseEntity.ok(responseDTO);
    }


    @PutMapping("/password")
    @PreAuthorize("hasAuthority('company:password_change')")
    public ResponseEntity<String> companyPasswordChange(@RequestBody ChangePasswordRequest request, Principal principal) {
        try {
            String result = companyService.changePassword(request, principal);
            return ResponseEntity.ok(result);
        } catch (AccessForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/application")
    @PreAuthorize("hasAuthority('company:read_application')")
    public List<JobApplicationResponse> getAllApplications(Principal principal) {
        Company company = companyService.getCompanyByUsername(principal.getName());

        List<JobApplication> companyApplications = jobApplicationService.getApplicationsByCompany(company);

        return companyApplications.stream()
                .map(JobApplicationResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/application/{userId}")
    @PreAuthorize("hasAuthority('company:read_application_by_userId')")
    public List<JobApplicationResponse> getApplicationByUserId(Principal principal, @PathVariable Long userId) {
        Company company = companyService.getCompanyByUsername(principal.getName());

        List<JobApplication> companyApplications = jobApplicationService.getApplicationsByCompanyAndUserId(company, userId);

        return companyApplications.stream()
                .map(JobApplicationResponse::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/application/respond/{userId}")
    @PreAuthorize("hasAuthority('company:respond')")
    public ResponseEntity<String> respondToApplication(Principal principal, @PathVariable Long userId, @RequestBody String response) {
        try {
            Company company = companyService.getCompanyByUsername(principal.getName());
            jobApplicationService.respondToApplication(company, userId, JobApplicationStatus.valueOf(response));
            return ResponseEntity.ok("Response sent successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid response status");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to respond to application");
        }
    }


    @PostMapping("/job")
    @PreAuthorize("hasAuthority('company:create')")
    public ResponseEntity<String> createJob(@RequestBody Job job, Principal principal) {
        Company company = companyService.getCompanyByUsername(principal.getName());
        Job createdJob = companyService.createJob(job, company);
        return ResponseEntity.status(HttpStatus.CREATED).body("Job created with ID: " + createdJob.getId());
    }

    @GetMapping("/job")
    @PreAuthorize("hasAuthority('company:read_job')")
    public ResponseEntity<List<JobResponse>> getAllJobsByCompany(Principal principal) {
        Company company = companyService.getCompanyByUsername(principal.getName());
        List<Job> jobs = companyService.getAllJobsByCompany(company);

        List<JobResponse> jobResponseList = jobs.stream()
                .map(JobResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(jobResponseList);
    }

    @PutMapping("/account/{companyId}")
    @PreAuthorize("hasAuthority('company:update_profile')")
    public ResponseEntity<String> updateCompanyProfile(@PathVariable Long companyId, @RequestBody UpdateCompanyProfileRequest request) {
        try {
            companyService.updateCompanyProfile(companyId, request);
            return ResponseEntity.ok("Company profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update company profile");
        }
    }

    @GetMapping("/download-cv/{userId}")
    @PreAuthorize("hasAuthority('company:cv_download')")
    public ResponseEntity<ByteArrayResource> downloadCV(@PathVariable Long userId) {
        return companyService.downloadApplicantCV(userId);
    }

}