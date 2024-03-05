package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.Location;
import com.AdmerkCorp.repository.LocationRepository;
import com.AdmerkCorp.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public void deleteLocationById(Long locationId) {
        locationRepository.deleteById(locationId);
    }

}