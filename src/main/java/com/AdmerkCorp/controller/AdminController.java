package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.request.UserRegisterRequest;
import com.AdmerkCorp.dto.response.AuthenticationResponse;
import com.AdmerkCorp.exception.ErrorResponse;
import com.AdmerkCorp.service.AuthenticationService;
import com.AdmerkCorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAllUsers() {
        return "All users retrieved successfully";
    }

    @GetMapping("/company")
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAllCompanies() {
        return "All companies retrieved successfully";
    }

    @GetMapping("/job")
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAllJobs() {
        return "All jobs retrieved successfully";
    }

    @GetMapping("/application")
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAllApplications() {
        return "All applications retrieved successfully";
    }

    @GetMapping("/account")
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAllAccounts() {
        return "All accounts retrieved successfully";
    }

    @GetMapping("/location")
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAllLocations() {
        return "All locations retrieved successfully";
    }

    @GetMapping("/social")
    @PreAuthorize("hasAuthority('admin:read')")
    public String getAllSocialAccounts() {
        return "All social accounts retrieved successfully";
    }

    @DeleteMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteUser(@PathVariable Long userId) {
        return "User deleted successfully";
    }

    @DeleteMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteCompany(@PathVariable Long companyId) {
        return "Company deleted successfully";
    }

    @DeleteMapping("/job/{jobId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteJob(@PathVariable Long jobId) {
        return "Job deleted successfully";
    }

    @DeleteMapping("/application/{applicationId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteApplication(@PathVariable Long applicationId) {
        return "Application deleted successfully";
    }

    @DeleteMapping("/account/{accountId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteAccount(@PathVariable Long accountId) {
        return "Account deleted successfully";
    }

    @DeleteMapping("/location/{locationId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteLocation(@PathVariable Long locationId) {
        return "Location deleted successfully";
    }

    @DeleteMapping("/social/{socialId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public String deleteSocial(@PathVariable Long socialId) {
        return "Social account deleted successfully";
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
