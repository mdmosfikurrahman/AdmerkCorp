package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateCompanyProfileRequest;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.exception.ResourceNotFoundException;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.model.Social;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.repository.CompanyRepository;
import com.AdmerkCorp.repository.UserRepository;
import com.AdmerkCorp.service.CompanyService;
import com.AdmerkCorp.service.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final JobService jobService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${cv.upload.directory}")
    private String cvUploadDirectory;

    @Override
    public Job createJob(Job job, Company company) {
        job.setCompany(company);
        job.setLocation(company.getLocation());
        job.setPostedOn(LocalDateTime.now());
        return jobService.createJob(job);
    }

    @Override
    public List<Job> getAllJobsByCompany(Company company) {
        return jobService.getAllJobsByCompany(company);
    }

    @Override
    public Company getCompanyByUsername(String username) {
        return companyRepository.findByName(username)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with username: " + username));
    }

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var company = (Company) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getPassword(), company.getPassword())) {
            throw new AccessForbiddenException("Wrong password");
        }

        company.setPassword(passwordEncoder.encode(request.getNewPassword()));

        companyRepository.save(company);

        return "Password changed Successfully!";
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public void deleteCompanyById(Long companyId) {
        companyRepository.deleteById(companyId);
    }

    @Override
    public void updateCompanyProfile(Long companyId, UpdateCompanyProfileRequest companyResponse) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + companyId));

        company.setCompanyName(companyResponse.getCompanyName());
        company.setCompanyMail(companyResponse.getCompanyMail());
        company.setWebsite(companyResponse.getWebsite());

        // Update Social
        Social social = company.getSocial();
        Social updateSocial = companyResponse.getSocial();
        social.setFacebook(updateSocial.getFacebook());
        social.setLinkedIn(updateSocial.getLinkedIn());
        social.setTwitter(updateSocial.getTwitter());
        social.setInstagram(updateSocial.getInstagram());
        social.setWhatsApp(updateSocial.getWhatsApp());

        // Update Location
        Location location = company.getLocation();
        Location updateLocation = companyResponse.getLocation();
        location.setCountry(updateLocation.getCountry());
        location.setState(updateLocation.getState());
        location.setCity(updateLocation.getCity());
        location.setAddress(updateLocation.getAddress());
        location.setZipCode(updateLocation.getZipCode());

        companyRepository.save(company);
    }

    @Override
    public ResponseEntity<ByteArrayResource> downloadApplicantCV(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        if (user.getCvFileName() == null) {
            throw new ResourceNotFoundException("CV not found for user with ID: " + userId);
        }

        try {
            Path filePath = Paths.get(cvUploadDirectory).resolve(user.getCvFileName());
            byte[] data = Files.readAllBytes(filePath);
            ByteArrayResource resource = new ByteArrayResource(data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(data.length);
            headers.setContentDispositionFormData("attachment", "CV_" + user.getRefugeeNumber() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CV file", e);
        }
    }

}