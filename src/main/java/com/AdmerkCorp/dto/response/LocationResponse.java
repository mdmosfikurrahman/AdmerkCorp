package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationResponse {

    private String country;
    private String state;
    private String city;
    private String address;
    private String zipCode;

    public LocationResponse(Location location) {
        this.country = location.getCountry();
        this.state = location.getState();
        this.city = location.getCity();
        this.address = location.getAddress();
        this.zipCode = location.getZipCode();
    }

}