package com.AdmerkCorp.service;

import com.AdmerkCorp.dto.request.AuthenticationRequest;
import com.AdmerkCorp.dto.response.AuthenticationResponse;
import com.AdmerkCorp.dto.request.CompanyRegisterRequest;
import com.AdmerkCorp.dto.request.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse registerUser(UserRegisterRequest request);
    AuthenticationResponse registerAdmin(UserRegisterRequest request);
    AuthenticationResponse registerCompany(CompanyRegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
