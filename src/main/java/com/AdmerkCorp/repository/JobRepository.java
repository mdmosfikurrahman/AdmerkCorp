package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.job.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCompany(Company company);

    @Query("SELECT j FROM Job j WHERE (:companyName IS NULL OR j.company.name = :companyName) " +
            "AND (:jobType IS NULL OR j.jobType = :jobType) " +
            "AND (:salaryDuration IS NULL OR j.salaryDuration = :salaryDuration) " +
            "AND (:category IS NULL OR j.category = :category) " +
            "AND (:experience IS NULL OR j.experience = :experience)")
    List<Job> getFilteredJobs(
            @Param("companyName") String companyName,
            @Param("jobType") JobType jobType,
            @Param("salaryDuration") SalaryDuration salaryDuration,
            @Param("category") Category category,
            @Param("experience") Experience experience);
}
