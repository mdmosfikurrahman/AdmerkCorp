package com.AdmerkCorp.service;

import com.AdmerkCorp.model.Location;

import java.util.List;

public interface LocationService {

    List<Location> getAllLocations();
    void deleteLocationById(Long locationId);

}