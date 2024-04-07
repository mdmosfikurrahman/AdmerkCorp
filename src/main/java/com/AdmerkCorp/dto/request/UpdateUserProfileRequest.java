package com.AdmerkCorp.dto.request;

import com.AdmerkCorp.dto.response.LocationResponse;
import com.AdmerkCorp.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateUserProfileRequest {
    private String firstName;
    private String lastName;
    private Boolean isRefugee;
    private LocalDate birthDate;
    private String email;
    private LocationResponse location;
    private String contactNumber;
    private MultipartFile profilePicture;
    private MultipartFile curriculumVitae;
}
