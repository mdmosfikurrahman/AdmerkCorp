package com.AdmerkCorp.dto.response;

import com.AdmerkCorp.model.Social;
import lombok.Data;

@Data
public class SocialResponse {
    private String facebook;
    private String linkedIn;
    private String twitter;
    private String instagram;
    private String whatsApp;

    public SocialResponse(Social social) {
        this.facebook = social.getFacebook();
        this.linkedIn = social.getLinkedIn();
        this.twitter = social.getTwitter();
        this.instagram = social.getInstagram();
        this.whatsApp = social.getWhatsApp();
    }
}
