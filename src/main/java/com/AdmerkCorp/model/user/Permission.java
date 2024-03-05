package com.AdmerkCorp.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    // Admin Permission Authority
    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    // Manager Permission Authority
    COMPANY_READ("company:read"),
    COMPANY_JOB_CREATE("company:create"),
    COMPANY_PASSWORD_CHANGE("company:password_change"),
    COMPANY_DELETE("company:delete"),
    COMPANY_JOB_DELETE("company:job_delete"),

    // User Permission Authority
    USER_READ("user:read"),
    USER_READ_JOBS("user:read_jobs"),
    USER_JOB_APPLY("user:job_apply"),
    USER_PASSWORD_CHANGE("user:password_change"),
    USER_DELETE("user:delete"),
    ;

    private final String permission;

}