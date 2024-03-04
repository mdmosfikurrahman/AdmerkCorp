package com.AdmerkCorp.model.job;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CoverLetter {

    private String title;
    private String description;

}
