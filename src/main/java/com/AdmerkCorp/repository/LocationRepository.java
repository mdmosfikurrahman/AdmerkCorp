package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
