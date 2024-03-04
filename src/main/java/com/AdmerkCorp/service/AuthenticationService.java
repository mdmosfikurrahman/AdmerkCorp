package com.AdmerkCorp.service;

import com.AdmerkCorp.dto.AuthenticationRequest;
import com.AdmerkCorp.dto.AuthenticationResponse;
import com.AdmerkCorp.dto.CompanyRegisterRequest;
import com.AdmerkCorp.dto.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse registerUser(UserRegisterRequest request);
    AuthenticationResponse registerCompany(CompanyRegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
