package com.AdmerkCorp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {

    private String password;
    private String newPassword;

}