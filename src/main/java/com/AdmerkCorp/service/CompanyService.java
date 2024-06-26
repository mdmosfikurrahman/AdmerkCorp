package com.AdmerkCorp.service;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateCompanyProfileRequest;
import com.AdmerkCorp.dto.response.CompanyResponse;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface CompanyService {

    Job createJob(Job job, Company company);
    List<Job> getAllJobsByCompany(Company company);
    Company getCompanyByUsername(String username);
    String changePassword(ChangePasswordRequest request, Principal connectedUser);
    void updateCompanyProfile(Long companyId, UpdateCompanyProfileRequest companyResponse) throws IOException;
    ByteArrayResource downloadApplicantCV(Long userId) throws IOException;
    ByteArrayResource downloadProfilePicture(Long companyId) throws IOException;
}