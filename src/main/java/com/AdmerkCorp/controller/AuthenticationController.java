package com.AdmerkCorp.controller;

import com.AdmerkCorp.dto.AuthenticationRequest;
import com.AdmerkCorp.dto.AuthenticationResponse;
import com.AdmerkCorp.dto.CompanyRegisterRequest;
import com.AdmerkCorp.dto.UserRegisterRequest;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.exception.ErrorResponse;
import com.AdmerkCorp.exception.ResourceNotFoundException;
import com.AdmerkCorp.exception.ValidationException;
import com.AdmerkCorp.service.AuthenticationService;
import com.AdmerkCorp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest request) {
        try {
            AuthenticationResponse response = authenticationService.registerUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException(CONFLICT, e);
        }
    }

    @PostMapping("/company/register")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyRegisterRequest request) {
        try {
            AuthenticationResponse response = authenticationService.registerCompany(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException(CONFLICT, e);
        }
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
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            return handleException(NOT_FOUND, e);
        } catch (AccessForbiddenException e) {
            return handleException(FORBIDDEN, e);
        } catch (Exception e) {
            return handleException(UNAUTHORIZED, e);
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    private ResponseEntity<ErrorResponse> handleException(HttpStatus status, Exception e) {
        return ResponseEntity.status(status)
                .body(
                        new ErrorResponse(
                                status.value(),
                                status.getReasonPhrase(),
                                e.getMessage()
                        )
                );
    }


}
