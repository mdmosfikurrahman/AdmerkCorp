package com.AdmerkCorp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {
    @GetMapping("/check")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read_jobs')")
    public String checkEndpoint() {
        return "User Endpoint";
    }
}
