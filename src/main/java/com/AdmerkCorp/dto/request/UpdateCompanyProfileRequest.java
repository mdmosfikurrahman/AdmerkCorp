package com.AdmerkCorp.dto.request;

import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.model.Social;
import lombok.Data;

@Data
public class UpdateCompanyProfileRequest {
    private String name;
    private String companyName;
    private String companyMail;
    private String website;
    private Social social;
    private Location location;
}
