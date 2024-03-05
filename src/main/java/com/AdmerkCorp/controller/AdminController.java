package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.request.UserRegisterRequest;
import com.AdmerkCorp.dto.response.*;
import com.AdmerkCorp.exception.ErrorResponse;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.model.Social;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final CompanyService companyService;
    private final JobService jobService;
    private final LocationService locationService;
    private final SocialService socialService;
    private final JobApplicationService jobApplicationService;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();

        List<User> filteredUsers = allUsers.stream()
                .filter(user -> "USER".equals(user.getRole().name()))
                .toList();

        List<UserResponse> userResponseList = filteredUsers.stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponseList);
    }


    @GetMapping("/company")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<Company> allCompanies = companyService.getAllCompanies();
        List<CompanyResponse> companyResponseList = allCompanies.stream()
                .map(CompanyResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(companyResponseList);
    }

    @GetMapping("/job")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<Job> allJobs = jobService.getAllJobs();
        List<JobResponse> jobResponseList = allJobs.stream()
                .map(JobResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(jobResponseList);
    }

    @GetMapping("/application")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<JobApplicationResponse>> getAllApplications() {
        List<JobApplication> jobApplications = jobApplicationService.getAllApplications();
        List<JobApplicationResponse> jobApplicationResponseList = jobApplications.stream()
                .map(JobApplicationResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(jobApplicationResponseList);
    }

    @GetMapping("/location")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        List<Location> locationList = locationService.getAllLocations();
        List<LocationResponse> locationResponseList = locationList.stream()
                .map(LocationResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(locationResponseList);
    }

    @GetMapping("/social")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<SocialResponse>> getAllSocialAccounts() {
        List<Social> socialList = socialService.getAllSocialAccounts();
        List<SocialResponse> socialResponseList = socialList.stream()
                .map(SocialResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(socialResponseList);
    }

    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteCompany(@PathVariable Long companyId) {
        try {
            companyService.deleteCompanyById(companyId);
            return ResponseEntity.ok("Company deleted successfully");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/job/{jobId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteJob(@PathVariable Long jobId) {
        try {
            jobService.deleteJobById(jobId);
            return ResponseEntity.ok("Job deleted successfully");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/application/{applicationId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteApplication(@PathVariable Long applicationId) {
        try {
            jobApplicationService.deleteApplicationById(applicationId);
            return ResponseEntity.ok("Application deleted successfully");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/location/{locationId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteLocation(@PathVariable Long locationId) {
        try {
            locationService.deleteLocationById(locationId);
            return ResponseEntity.ok("Location deleted successfully");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @DeleteMapping("/social/{socialId}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteSocial(@PathVariable Long socialId) {
        try {
            socialService.deleteSocialById(socialId);
            return ResponseEntity.ok("Social account deleted successfully");
        } catch (Exception e) {
            return handleException(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> createAdminAccount(@RequestBody Map<String, String> adminDetails) {
        String email = adminDetails.get("login_email");
        String username = adminDetails.get("login_name");
        String password = adminDetails.get("password");

        UserRegisterRequest adminUser = UserRegisterRequest.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        try {
            AuthenticationResponse response = authenticationService.registerAdmin(adminUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        new ErrorResponse(
                                HttpStatus.CONFLICT.value(),
                                HttpStatus.CONFLICT.getReasonPhrase(),
                                e.getMessage()
                        )
                );
    }

}