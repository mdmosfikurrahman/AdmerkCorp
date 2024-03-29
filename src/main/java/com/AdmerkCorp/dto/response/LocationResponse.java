package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.Location;
import lombok.Data;

@Data
public class LocationResponse {

    private String country;
    private String state;
    private String division;
    private String city;
    private String address;
    private String zipCode;

    public LocationResponse(Location location) {
        this.country = location.getCountry();
        this.state = location.getState();
        this.division = location.getDivision();
        this.city = location.getCity();
        this.address = location.getAddress();
        this.zipCode = location.getZipCode();
    }

}