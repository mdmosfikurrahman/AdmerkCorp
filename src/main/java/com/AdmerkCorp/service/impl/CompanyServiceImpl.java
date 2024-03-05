package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.repository.CompanyRepository;
import com.AdmerkCorp.service.CompanyService;
import com.AdmerkCorp.service.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final JobService jobService;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Job createJob(Job job, Company company) {
        job.setCompany(company);
        return jobService.createJob(job);
    }

    @Override
    public List<Job> getAllJobsByCompany(Company company) {
        return jobService.getAllJobsByCompany(company);
    }

    @Override
    public Company getCompanyByUsername(String username) {
        return companyRepository.findByName(username)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with username: " + username));
    }

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var company = (Company) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getCurrentPassword(), company.getPassword())) {
            throw new AccessForbiddenException("Wrong password");
        }

        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new AccessForbiddenException("Password doesn't match");
        }

        company.setPassword(passwordEncoder.encode(request.getNewPassword()));

        companyRepository.save(company);

        return "Password changed Successfully!";
    }
}
