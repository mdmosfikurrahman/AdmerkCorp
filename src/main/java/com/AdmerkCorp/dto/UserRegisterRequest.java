package com.AdmerkCorp.dto;

import com.AdmerkCorp.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String email;
    private String username;
    private String password;
    private boolean isRefugee;
    private Location location;
}
