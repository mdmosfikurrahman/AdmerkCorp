package com.AdmerkCorp.config.security;

import com.AdmerkCorp.repository.CompanyTokenRepository;
import com.AdmerkCorp.repository.UserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final UserTokenRepository userTokenRepository;
    private final CompanyTokenRepository companyTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String jwt = authHeader.substring(7);

        var userToken = userTokenRepository.findByToken(jwt);

        if (userToken.isPresent()) {
            userToken.get().setExpired(true);
            userToken.get().setRevoked(true);
            userTokenRepository.save(userToken.get());
            SecurityContextHolder.clearContext();
            return;
        }

        var companyToken = companyTokenRepository.findByToken(jwt);

        if (companyToken.isPresent()) {
            companyToken.get().setExpired(true);
            companyToken.get().setRevoked(true);
            companyTokenRepository.save(companyToken.get());
            SecurityContextHolder.clearContext();
        }
    }
}