package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.repository.LocationRepository;
import com.AdmerkCorp.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public void deleteLocationById(Long locationId) {
        locationRepository.deleteById(locationId);
    }
}