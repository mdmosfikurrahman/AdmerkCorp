package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.dto.request.ChangePasswordRequest;
import com.AdmerkCorp.dto.request.UpdateUserProfileRequest;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.exception.ResourceAlreadyExistsException;
import com.AdmerkCorp.exception.ResourceNotFoundException;
import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.model.job.Job;
import com.AdmerkCorp.repository.UserRepository;
import com.AdmerkCorp.service.JobService;
import com.AdmerkCorp.service.UserService;
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
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JobService jobService;
    private final PasswordEncoder passwordEncoder;

    @Value("${upload.directory.cv}")
    private String cvDirectory;

    @Value("${upload.directory.profilePicture}")
    private String profilePictureDirectory;

    @Override
    public void validateUser(String usernameOrEmail) {
        User existingUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (existingUser == null) {
            throw new ResourceAlreadyExistsException("User with this username already exists");
        }

    }

    @Override
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AccessForbiddenException("Wrong password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return "Password changed Successfully!";
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    public void updateUserProfile(Long userId, UpdateUserProfileRequest request) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRefugee(request.getIsRefugee());
        user.setEmail(request.getEmail());
        user.setBirthDate(request.getBirthDate());
        user.setContactNumber(request.getContactNumber());

        if (request.getProfilePicture() != null && !request.getProfilePicture().isEmpty()) {
            String profilePictureFileName = saveProfilePicture(request.getProfilePicture(), user);
            user.setProfilePicture(profilePictureFileName);
        }

        if (request.getCurriculumVitae() != null && !request.getCurriculumVitae().isEmpty()) {
            String curriculumVitaeFileName = saveCurriculumVitae(request.getCurriculumVitae(), user);
            user.setCvUploaded(true);
            user.setCvFileName(curriculumVitaeFileName);
        }

        Location location = user.getLocation();
        location.setCountry(request.getLocation().getCountry());
        location.setState(request.getLocation().getState());
        location.setCity(request.getLocation().getCity());
        location.setAddress(request.getLocation().getAddress());
        location.setZipCode(request.getLocation().getZipCode());

        userRepository.save(user);
    }

    private String saveProfilePicture(MultipartFile profilePicture, User user) throws IOException {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(profilePicture.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String newFileName = user.getRefugeeNumber() + "." + fileExtension;

        Path uploadPath = Paths.get(profilePictureDirectory).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(newFileName);
        Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return newFileName;
    }

    private String saveCurriculumVitae(MultipartFile curriculumVitae, User user) throws IOException {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(curriculumVitae.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        String newFileName = "CV_" + user.getFirstName() + " " + user.getLastName() + "." + fileExtension;

        Path uploadPath = Paths.get(cvDirectory).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(newFileName);
        Files.copy(curriculumVitae.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return newFileName;
    }

    @Override
    public ByteArrayResource downloadCV(Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Path cvPath = Paths.get(cvDirectory).toAbsolutePath().normalize().resolve(user.getCvFileName());
        byte[] cvBytes = Files.readAllBytes(cvPath);

        return new ByteArrayResource(cvBytes);
    }

    @Override
    public ByteArrayResource downloadProfilePicture(Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Path profilePicturePath = Paths.get(profilePictureDirectory).toAbsolutePath().normalize().resolve(user.getProfilePicture());

        byte[] profilePictureBytes = Files.readAllBytes(profilePicturePath);

        return new ByteArrayResource(profilePictureBytes);
    }


}
