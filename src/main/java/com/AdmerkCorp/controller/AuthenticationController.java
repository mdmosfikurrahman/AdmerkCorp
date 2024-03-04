package com.AdmerkCorp.controller;

import com.AdmerkCorp.exception.ValidationException;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.User;
import com.AdmerkCorp.service.CompanyService;
import com.AdmerkCorp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final CompanyService companyService;

    @PostMapping("/user/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/company/register")
    public ResponseEntity<Company> registerCompany(@RequestBody Company company) {
        Company registeredCompany = companyService.registerCompany(company);
        return ResponseEntity.ok(registeredCompany);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateUser(@RequestParam(name = "usernameOrEmail") String usernameOrEmail) {
        try {
            userService.validateUser(usernameOrEmail);
            return ResponseEntity.ok("Validated");
        } catch (ValidationException ex) {
            return ResponseEntity.badRequest().body(ex.getErrors());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam(name = "username") String username,
                                       @RequestParam(name = "password") String password) {
        try {
            userService.authenticateUser(username, password);
            return ResponseEntity.ok("Login successful");
        } catch (ValidationException ex) {
            return ResponseEntity.badRequest().body(ex.getErrors());
        }
    }


}
