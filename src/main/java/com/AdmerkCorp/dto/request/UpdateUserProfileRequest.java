package com.AdmerkCorp.dto.request;

import com.AdmerkCorp.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateUserProfileRequest {

    private String firstName;

    private String lastName;

    private Boolean isRefugee;

    private LocalDate birthDate;

    private String email;

    private Location location;

    private String contactNumber;

}
