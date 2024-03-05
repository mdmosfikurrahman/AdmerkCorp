package com.AdmerkCorp.service;

import com.AdmerkCorp.model.Social;

import java.util.List;

public interface SocialService {

    List<Social> getAllSocialAccounts();
    void deleteSocialById(Long socialId);

}