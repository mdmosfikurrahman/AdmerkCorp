package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.user.User;
import lombok.Data;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String role;
    private LocationResponse location;

    public UserResponse(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role = user.getRole().name();
        this.location = new LocationResponse(user.getLocation());
    }
}

