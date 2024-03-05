package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.Company;
import lombok.Data;

@Data
public class CompanyResponse {
    private String name;
    private String companyName;
    private String companyMail;
    private String website;
    private SocialResponse social;
    private LocationResponse location;

    public CompanyResponse(Company company) {
        this.name = company.getName();
        this.companyName = company.getCompanyName();
        this.companyMail = company.getCompanyMail();
        this.website = company.getWebsite();
        this.social = new SocialResponse(company.getSocial());
        this.location = new LocationResponse(company.getLocation());
    }
}
