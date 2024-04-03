package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.config.security.JwtService;
import com.AdmerkCorp.dto.request.AuthenticationRequest;
import com.AdmerkCorp.dto.response.AuthenticationResponse;
import com.AdmerkCorp.dto.request.CompanyRegisterRequest;
import com.AdmerkCorp.dto.request.UserRegisterRequest;
import com.AdmerkCorp.dto.response.LoginData;
import com.AdmerkCorp.exception.AccessForbiddenException;
import com.AdmerkCorp.exception.ResourceAlreadyExistsException;
import com.AdmerkCorp.exception.ResourceNotFoundException;
import com.AdmerkCorp.model.Company;
import com.AdmerkCorp.model.token.CompanyToken;
import com.AdmerkCorp.model.token.UserToken;
import com.AdmerkCorp.model.user.Role;
import com.AdmerkCorp.model.user.User;
import com.AdmerkCorp.repository.CompanyRepository;
import com.AdmerkCorp.repository.CompanyTokenRepository;
import com.AdmerkCorp.repository.UserTokenRepository;
import com.AdmerkCorp.repository.UserRepository;
import com.AdmerkCorp.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.*;
import java.util.UUID;

import static com.AdmerkCorp.model.token.TokenType.BEARER;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTokenRepository userTokenRepository;
    private final CompanyTokenRepository companyTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse registerUser(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with this username already exists");
        }

        ZonedDateTime zonedDateTime = ZonedDateTime.parse(request.getBirthDate());

        ZoneId toZone = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(6)); // UTC+6
        ZonedDateTime convertedZoneDateTime = zonedDateTime.withZoneSameInstant(toZone);

        LocalDate convertedLocalDate = convertedZoneDateTime.getHour() >= 12 ?
                convertedZoneDateTime.plusDays(1).toLocalDate() :
                convertedZoneDateTime.toLocalDate();

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthDate(convertedLocalDate)
                .email(request.getEmail())
                .username(request.getUsername())
                .isRefugee(request.getIsRefugee())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .location(request.getLocation())
                .refugeeNumber(request.getIsRefugee() ? generateRefugeeNumber(request) : null)
                .contactNumber(request.getContactNumber())
                .cvUploaded(false)
                .build();

        return getAuthenticationResponseForAdminAndUser(request, user);
    }

    private String generateRefugeeNumber(UserRegisterRequest request) {
        String uuid = UUID.randomUUID().toString();
        String initials = String.valueOf(request.getFirstName().charAt(0)).toUpperCase() + String.valueOf(request.getLastName().charAt(0)).toUpperCase();
        return initials + "-" + uuid.substring(0, 6).toUpperCase();
    }


    @Override
    public AuthenticationResponse registerAdmin(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("User with this username already exists");
        }

        var user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        return getAuthenticationResponseForAdminAndUser(request, user);
    }

    @Override
    public AuthenticationResponse registerCompany(CompanyRegisterRequest request) {
        if (companyRepository.findByName(request.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Company with this name already exists");
        }

        var company = Company.builder()
                .name(request.getName())
                .companyName(request.getCompanyName())
                .companyMail(request.getCompanyMail())
                .password(passwordEncoder.encode(request.getPassword()))
                .website(request.getWebsite())
                .social(request.getSocial())
                .location(request.getLocation())
                .role(Role.COMPANY)
                .build();

        return getAuthenticationResponseForCompany(request, company);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            var companyOptional = companyRepository.findByName(request.getUsername());

            if (companyOptional.isEmpty()) {
                throw new ResourceNotFoundException("User not found");
            }

            Company company = companyOptional.get();

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );
            } catch (AuthenticationException e) {
                throw new AccessForbiddenException("Invalid password");
            }

            var jwtToken = jwtService.generateToken(company);
            var refreshToken = jwtService.generateRefreshToken(company);

            revokeAllCompanyTokens(company);
            saveCompanyToken(company, jwtToken);

            var loginData = LoginData.builder()
                    .email(company.getCompanyMail())
                    .username(company.getName())
                    .name(company.getCompanyName())
                    .role(String.valueOf(Role.COMPANY).toLowerCase())
                    .isRefugee(false)
                    .build();

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                .tokenType("Bearer")
                    .loginData(loginData)
                    .build();
        }

        User user = userOptional.get();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new AccessForbiddenException("Invalid password");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        var loginData = LoginData.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getFirstName() + ' ' + user.getLastName())
                .role(String.valueOf(Role.USER).toLowerCase())
                .isRefugee(user.isRefugee())
                .build();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .loginData(loginData)
                .build();
    }


    private void saveUserToken(User user, String jwtToken) {
        var token = UserToken.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(BEARER)
                .expired(false)
                .revoked(false)
                .build();
        userTokenRepository.save(token);
    }

    private void saveCompanyToken(Company company, String jwtToken) {
        var token = CompanyToken.builder()
                .company(company)
                .token(jwtToken)
                .tokenType(BEARER)
                .expired(false)
                .revoked(false)
                .build();
        companyTokenRepository.save(token);
    }

    private AuthenticationResponse getAuthenticationResponseForAdminAndUser(UserRegisterRequest request, User user) {
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);

        var loginData = LoginData.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .name(user.getFirstName() + ' ' + user.getLastName())
                .role(String.valueOf(Role.USER).toLowerCase())
                .isRefugee(user.isRefugee())
                .build();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .loginData(loginData)
                .build();
    }

    private AuthenticationResponse getAuthenticationResponseForCompany(CompanyRegisterRequest request, Company company) {
        var savedCompany = companyRepository.save(company);
        var jwtToken = jwtService.generateToken(company);
        var refreshToken = jwtService.generateRefreshToken(company);
        saveCompanyToken(savedCompany, jwtToken);

        var loginData = LoginData.builder()
                .email(request.getCompanyMail())
                .username(request.getName())
                .name(request.getName())
                .role(String.valueOf(Role.COMPANY).toLowerCase())
                .isRefugee(false)
                .build();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .loginData(loginData)
                .build();
    }

    private void revokeAllCompanyTokens(Company company) {
        var validCompanyTokens = companyTokenRepository.findAllValidTokenByCompany(company.getId());
        if (validCompanyTokens.isEmpty())
            return;
        validCompanyTokens.forEach(companyToken -> {
            companyToken.setExpired(true);
            companyToken.setRevoked(true);
        });
        companyTokenRepository.saveAll(validCompanyTokens);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = userTokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(userToken -> {
            userToken.setExpired(true);
            userToken.setRevoked(true);
        });
        userTokenRepository.saveAll(validUserTokens);
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null) {
            var userOptional = this.userRepository.findByUsername(username);
            var companyOptional = this.companyRepository.findByName(username);

            if (userOptional.isPresent()) {
                refreshUserToken(response, refreshToken, userOptional.get());
            } else if (companyOptional.isPresent()) {
                refreshCompanyToken(response, refreshToken, companyOptional.get());
            }
        }
    }

    private void refreshUserToken(HttpServletResponse response, String refreshToken, User user) throws IOException {
        if (jwtService.isTokenValid(refreshToken, user)) {
            var accessToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            var loginData = LoginData.builder()
                    .email(user.getEmail())
                    .username(user.getUsername())
                    .name(user.getFirstName() + ' ' + user.getLastName())
                    .role(String.valueOf(Role.USER).toLowerCase())
                    .isRefugee(user.isRefugee())
                    .build();
            var authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                .tokenType("Bearer")
                    .loginData(loginData)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }

    private void refreshCompanyToken(HttpServletResponse response, String refreshToken, Company company) throws IOException {
        if (jwtService.isTokenValid(refreshToken, company)) {
            var accessToken = jwtService.generateToken(company);
            revokeAllCompanyTokens(company);
            saveCompanyToken(company, accessToken);
            var loginData = LoginData.builder()
                    .email(company.getCompanyMail())
                    .username(company.getName())
                    .name(company.getCompanyName())
                    .role(String.valueOf(Role.COMPANY).toLowerCase())
                    .isRefugee(false)
                    .build();
            var authResponse = AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                .tokenType("Bearer")
                    .loginData(loginData)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }

}