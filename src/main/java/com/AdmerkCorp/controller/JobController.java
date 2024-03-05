package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.response.JobResponse;
import com.AdmerkCorp.model.job.*;
import com.AdmerkCorp.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<Job> allJobs = jobService.getAllJobs();
        List<JobResponse> jobResponseList = allJobs.stream()
                .map(JobResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(jobResponseList);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long jobId) {
        Job job = jobService.getJobById(jobId);
        JobResponse jobResponse = new JobResponse(job);
        return ResponseEntity.ok(jobResponse);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<JobResponse>> getJobsByQuery(
            @RequestParam(name = "companyName", required = false) String companyName,
            @RequestParam(name = "jobType", required = false) JobType jobType,
            @RequestParam(name = "salaryDuration", required = false) SalaryDuration salaryDuration,
            @RequestParam(name = "category", required = false) Category category,
            @RequestParam(name = "experience", required = false) Experience experience) {

        List<Job> filteredJobs = jobService.getFilteredJobs(companyName, jobType, salaryDuration, category, experience);

        List<JobResponse> jobResponseList = filteredJobs.stream()
                .map(JobResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(jobResponseList);
    }

}