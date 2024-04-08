package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateCompanyProfileRequest;
import com.AdmerkCorp.dto.response.*;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.Social;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.model.job.JobApplicationStatus;
import com.AdmerkCorp.service.CompanyService;
import com.AdmerkCorp.service.JobApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
@PreAuthorize("hasRole('COMPANY')")
@RequiredArgsConstructor
public class CompanyController {

    private final ObjectMapper objectMapper;
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
    public ResponseEntity<JobApplicationResponse> getApplicationByUserId(Principal principal, @PathVariable Long userId) {
        Company company = companyService.getCompanyByUsername(principal.getName());

        JobApplication jobApplication = jobApplicationService.getApplicationsByCompanyAndUserId(company, userId);

        if (jobApplication != null) {
            JobApplicationResponse applicationResponse = new JobApplicationResponse(jobApplication);
            return ResponseEntity.ok(applicationResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<String> updateCompanyProfile(
            @PathVariable Long companyId,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestParam(value = "companyName", required = false) String companyName,
            @RequestParam(value = "companyMail", required = false) String companyMail,
            @RequestParam(value = "social", required = false) String social,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "website", required = false) String website) {

        try {
            LocationResponse locationResponse = null;
            if (Objects.nonNull(location)) {
                locationResponse = objectMapper.readValue(location, LocationResponse.class);
            }

            Social socialResponse = null;
            if (Objects.nonNull(social)) {
                socialResponse = objectMapper.readValue(social, Social.class);
            }

            UpdateCompanyProfileRequest request = new UpdateCompanyProfileRequest(
                    companyName, companyMail, website, socialResponse, locationResponse, profilePicture);

            companyService.updateCompanyProfile(companyId, request);
            return ResponseEntity.ok("Company profile updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update company profile");
        }
    }

    @GetMapping("/download-cv/{userId}")
    @PreAuthorize("hasAuthority('company:cv_download')")
    public ResponseEntity<Resource> downloadCV(@PathVariable Long userId) {
        try {
            ByteArrayResource response = companyService.downloadApplicantCV(userId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/account/profile-picture/{companyId}")
    @PreAuthorize("hasAnyAuthority('company:read','user:read')")
    public ResponseEntity<Resource> downloadProfilePicture(@PathVariable Long companyId) {
        try {
            ByteArrayResource response = companyService.downloadProfilePicture(companyId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}