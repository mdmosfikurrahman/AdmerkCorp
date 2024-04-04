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
    COMPANY_READ_APPLICATION("company:read_application"),
    COMPANY_READ_APPLICATION_BY_USER_ID("company:read_application_by_userId"),
    COMPANY_READ_JOB("company:read_job"),
    COMPANY_UPDATE_PROFILE("company:update_profile"),
    COMPANY_JOB_CREATE("company:create"),
    COMPANY_JOB_RESPOND("company:respond"),
    COMPANY_CV_DOWNLOAD("company:cv_download"),
    COMPANY_PASSWORD_CHANGE("company:password_change"),
    COMPANY_DELETE("company:delete"),
    COMPANY_JOB_DELETE("company:job_delete"),

    // User Permission Authority
    USER_READ("user:read"),
    USER_UPDATE_PROFILE("user:update_profile"),
    USER_READ_JOBS("user:read_jobs"),
    USER_JOB_APPLY("user:job_apply"),
    USER_PASSWORD_CHANGE("user:password_change"),
    USER_DELETE("user:delete"),
    USER_CV_UPLOAD("user:cv_upload"),
    USER_CV_DOWNLOAD("user:cv_download"),
    ;

    private final String permission;

}