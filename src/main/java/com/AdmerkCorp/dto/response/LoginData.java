package com.AdmerkCorp.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginData {

    private String username;
    private String name;
    private String email;
    private String role;
    private Boolean isRefugee;
    private String refugeeNumberOrAddress;
    private String profilePictureUrl;

}
