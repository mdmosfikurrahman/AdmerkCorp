package com.AdmerkCorp.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.AdmerkCorp.model.user.Permission.*;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER(
            Set.of(
                    USER_READ,
                    USER_UPDATE_PROFILE,
                    USER_READ_JOBS,
                    USER_JOB_APPLY,
                    USER_PASSWORD_CHANGE,
                    USER_CV_UPLOAD,
                    USER_CV_DOWNLOAD
            )
    ),

    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,

                    USER_READ_JOBS,
                    USER_JOB_APPLY,
                    USER_PASSWORD_CHANGE,
                    USER_DELETE,

                    COMPANY_READ,
                    COMPANY_PASSWORD_CHANGE,
                    COMPANY_JOB_CREATE,
                    COMPANY_DELETE,
                    COMPANY_JOB_DELETE
            )
    ),

    COMPANY(
            Set.of(
                    COMPANY_READ,
                    COMPANY_UPDATE_PROFILE,
                    COMPANY_PASSWORD_CHANGE,
                    COMPANY_JOB_CREATE,
                    COMPANY_JOB_RESPOND,
                    COMPANY_CV_DOWNLOAD,
                    COMPANY_READ_APPLICATION,
                    COMPANY_READ_APPLICATION_BY_USER_ID,
                    COMPANY_READ_JOB
            )
    )

    ;

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}