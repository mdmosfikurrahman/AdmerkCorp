package com.AdmerkCorp.dto.request;

import com.AdmerkCorp.dto.response.LocationResponse;
import com.AdmerkCorp.dto.response.SocialResponse;
import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.model.Social;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompanyProfileRequest {
    private String companyName;
    private String companyMail;
    private String website;
    private Social social;
    private LocationResponse location;
    private MultipartFile profilePicture;
}
