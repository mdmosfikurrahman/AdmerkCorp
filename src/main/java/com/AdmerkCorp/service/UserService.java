package com.AdmerkCorp.service;

import com.AdmerkCorp.model.User;

public interface UserService {
    User registerUser(User user);
    boolean validateUser(String usernameOrEmail);
    User authenticateUser(String username, String password);
}
