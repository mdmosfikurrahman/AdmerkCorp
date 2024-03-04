package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByCompanyNameOrCompanyMail(String companyName, String companyMail);
}
