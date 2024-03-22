package com.AdmerkCorp.dto.request;

import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.model.Social;
import com.AdmerkCorp.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRegisterRequest {

    private String name;
    private String companyName;
    private String companyMail;
    private String password;
    private String website;
    private Social social;
    private Location location;

}