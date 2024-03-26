package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUser(User user);

    List<JobApplication> findByJobCompany(Company company);

    @Query("SELECT ja FROM JobApplication ja WHERE ja.user.id = :userId AND ja.job.company.id = :companyId")
    JobApplication findByUserIdAndCompanyId(@Param("userId") Long userId, @Param("companyId") Long companyId);

    List<JobApplication> findByUserAndJob(User user, Job job);

}