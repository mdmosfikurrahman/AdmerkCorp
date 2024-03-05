package com.AdmerkCorp.service;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;

import java.security.Principal;
import java.util.List;

public interface CompanyService {
    Job createJob(Job job, Company company);
    List<Job> getAllJobsByCompany(Company company);
    Company getCompanyByUsername(String username);
    String changePassword(ChangePasswordRequest request, Principal connectedUser);
}
