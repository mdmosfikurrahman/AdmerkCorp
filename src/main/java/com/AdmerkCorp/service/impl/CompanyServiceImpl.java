package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.repository.CompanyRepository;
import com.AdmerkCorp.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company registerCompany(Company company) {
        return companyRepository.save(company);
    }
}
