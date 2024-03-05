package com.AdmerkCorp.service.impl;

import com.AdmerkCorp.model.Social;
import com.AdmerkCorp.repository.SocialRepository;
import com.AdmerkCorp.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final SocialRepository socialRepository;

    @Override
    public List<Social> getAllSocialAccounts() {
        return socialRepository.findAll();
    }

    @Override
    public void deleteSocialById(Long socialId) {
        socialRepository.deleteById(socialId);
    }

}