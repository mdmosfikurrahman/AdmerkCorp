package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.job.Job;
import lombok.Data;

@Data
public class JobResponse {
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

    public JobResponse(Job job) {
        this.jobTitle = job.getJobTitle();
        this.jobType = job.getJobType().name();
        this.salaryAmount = job.getSalaryAmount();
        this.salaryDuration = job.getSalaryDuration().name();
        this.category = job.getCategory().name();
        this.subCategory = job.getSubCategory().name();
        this.overview = job.getOverview();
        this.jobDescription = job.getJobDescription();
        this.experience = job.getExperience().name();
        this.responsibility = job.getResponsibility();
        this.requiredSkills = job.getRequiredSkills();
        this.benefits = job.getBenefits();
        this.jobUrl = job.getJobUrl();
        this.company = new CompanyResponse(job.getCompany());
    }
}
