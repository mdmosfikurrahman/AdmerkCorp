package com.AdmerkCorp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @GetMapping("/check")
    public String checkEndpoint() {
        return "Company Endpoint";
    }
}
