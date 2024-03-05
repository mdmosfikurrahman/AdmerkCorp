package com.AdmerkCorp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
@PreAuthorize("hasAnyRole('ADMIN', 'COMPANY')")
public class CompanyController {
    @GetMapping("/check")
    @PreAuthorize("hasAnyAuthority('admin:read', 'company:read')")
    public String checkEndpoint() {
        return "Company Endpoint";
    }
}
