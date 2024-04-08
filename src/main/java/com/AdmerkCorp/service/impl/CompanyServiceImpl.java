package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateCompanyProfileRequest;
import com.AdmerkCorp.dto.response.LocationResponse;
import com.AdmerkCorp.dto.response.SocialResponse;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final JobService jobService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.directory.cv}")
    private String cvDirectory;

    @Value("${upload.directory.profilePicture}")
    private String profilePictureDirectory;

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
    public void updateCompanyProfile(Long companyId, UpdateCompanyProfileRequest request) throws IOException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with ID: " + companyId));

        company.setCompanyName(request.getCompanyName());
        company.setCompanyMail(request.getCompanyMail());
        company.setWebsite(request.getWebsite());

        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            String profilePictureFileName = saveProfilePicture(request.getProfilePicture(), company);
            company.setProfilePicture(profilePictureFileName);
        }

        // Update Social
        Social social = company.getSocial();
        Social updateSocial = request.getSocial();
        social.setFacebook(updateSocial.getFacebook());
        social.setLinkedIn(updateSocial.getLinkedIn());
        social.setTwitter(updateSocial.getTwitter());
        social.setInstagram(updateSocial.getInstagram());
        social.setWhatsApp(updateSocial.getWhatsApp());

        // Update Location
        Location location = company.getLocation();
        LocationResponse updateLocation = request.getLocation();
        location.setCountry(updateLocation.getCountry());
        location.setState(updateLocation.getState());
        location.setCity(updateLocation.getCity());
        location.setAddress(updateLocation.getAddress());
        location.setZipCode(updateLocation.getZipCode());

        companyRepository.save(company);
    }

    private String saveProfilePicture(MultipartFile profilePicture, Company company) throws IOException {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(profilePicture.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String newFileName = company.getName() + "." + fileExtension;

        Path uploadPath = Paths.get(profilePictureDirectory).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(newFileName);
        Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return newFileName;
    }

    @Override
    public ByteArrayResource downloadApplicantCV(Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Path cvPath = Paths.get(cvDirectory).toAbsolutePath().normalize().resolve(user.getCvFileName());
        byte[] cvBytes = Files.readAllBytes(cvPath);

        return new ByteArrayResource(cvBytes);
    }

    @Override
    public ByteArrayResource downloadProfilePicture(Long companyId) throws IOException {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + companyId));

        Path profilePicturePath = Paths.get(profilePictureDirectory).toAbsolutePath().normalize().resolve(company.getProfilePicture());

        byte[] profilePictureBytes = Files.readAllBytes(profilePicturePath);

        return new ByteArrayResource(profilePictureBytes);
    }

}