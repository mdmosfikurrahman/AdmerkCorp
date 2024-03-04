package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.user.Role;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.job.JobApplication;
import com.AdmerkCorp.repository.CompanyRepository;
import com.AdmerkCorp.service.CompanyService;
import com.AdmerkCorp.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final JobService jobService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Company registerCompany(Company company) {
        String encryptedPassword = passwordEncoder.encode(company.getPassword());
        company.setPassword(encryptedPassword);
        company.setRole(Role.ROLE_COMPANY);
        return companyRepository.save(company);
    }

    @Override
    public Company authenticateCompany(String companyName, String password) {
        Company company = companyRepository.findByName(companyName);
        if (company != null && passwordEncoder.matches(password, company.getPassword())) {
            return company;
        }
        return null;
    }

    @Override
    public Job createJob(Job job, Company company) {
        job.setCompany(company);
        return jobService.createJob(job);
    }

    @Override
    public List<JobApplication> getJobApplicants(Long jobId) {
        return jobService.getJobApplicants(jobId);
    }
}
