package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.exception.ValidationException;
import com.AdmerkCorp.model.User;
import com.AdmerkCorp.repository.UserRepository;
import com.AdmerkCorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public User registerUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    @Override
    public boolean validateUser(String usernameOrEmail) {
        User existingUser = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);

        if (existingUser == null) {
            throw new ValidationException("Invalid user username or email");
        }

        return true;
    }

    @Override
    public User authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new ValidationException("Invalid username or password");
        }
        return user;
    }

}
