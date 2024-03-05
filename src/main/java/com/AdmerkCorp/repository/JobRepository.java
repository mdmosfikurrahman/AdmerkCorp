package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCompany(Company company);

}
