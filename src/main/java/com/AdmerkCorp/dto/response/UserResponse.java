package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.user.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String username;
    private LocationResponse location;
    private Boolean isRefugee;
    private String refugeeNumber;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.birthDate = user.getBirthDate();
        this.location = new LocationResponse(user.getLocation());
        this.isRefugee = user.isRefugee();
        this.refugeeNumber = user.getRefugeeNumber();
    }

}