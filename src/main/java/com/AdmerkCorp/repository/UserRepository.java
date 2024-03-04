package com.AdmerkCorp.repository;

import com.AdmerkCorp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameOrEmail(String username, String email);
    User findByUsername(String username);
}
