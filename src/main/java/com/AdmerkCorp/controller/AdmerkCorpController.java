package com.AdmerkCorp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdmerkCorpController {

    @GetMapping("/")
    public String getAPIDoc() {
        return "apiDoc";
    }

    @GetMapping("/terms")
    public String getTermsAndConditions() {
        return "terms";
    }

}