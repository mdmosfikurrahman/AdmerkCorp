package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.job.Job;
import lombok.Data;

@Data
public class JobResponse {

    private Long jobId;
    private String jobTitle;
    private String jobType;
    private int salaryAmount;
    private String salaryDuration;
    private String category;
    private String subCategory;
    private String overview;
    private String jobDescription;
    private String experience;
    private String responsibility;
    private String requiredSkills;
    private String benefits;
    private String jobUrl;
    private CompanyResponse company;
    private LocationResponse location;

    public JobResponse(Job job) {
        this.jobId = job.getId();
        this.jobTitle = job.getJobTitle();
        this.jobType = job.getJobType() != null ? job.getJobType().name() : null;
        this.salaryAmount = job.getSalaryAmount();
        this.salaryDuration = job.getSalaryDuration() != null ? job.getSalaryDuration().name() : null;
        this.category = job.getCategory() != null ? job.getCategory().name() : null;
        this.subCategory = job.getSubCategory() != null ? job.getSubCategory().name() : null;
        this.overview = job.getOverview();
        this.jobDescription = job.getJobDescription();
        this.experience = job.getExperience() != null ? job.getExperience().name() : null;
        this.responsibility = job.getResponsibility();
        this.requiredSkills = job.getRequiredSkills();
        this.benefits = job.getBenefits();
        this.jobUrl = job.getJobUrl();
        this.company = job.getCompany() != null ? new CompanyResponse(job.getCompany()) : null;
        this.location = job.getLocation() != null ? new LocationResponse(job.getLocation()) : null;
    }

}