package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.user.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String username;
    private LocationResponse location;

    public UserResponse(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.birthDate = user.getBirthDate();
        this.location = new LocationResponse(user.getLocation());
    }

}