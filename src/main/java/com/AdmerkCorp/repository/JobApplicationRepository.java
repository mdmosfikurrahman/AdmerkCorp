package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUser(User user);

    List<JobApplication> findByJobCompany(Company company);

}