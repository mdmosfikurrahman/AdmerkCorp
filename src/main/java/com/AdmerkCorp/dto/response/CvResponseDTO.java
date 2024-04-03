package com.AdmerkCorp.dto.response;

import lombok.Data;

@Data
public class CvResponseDTO {
    private byte[] fileContent;
    private String fileName;

    public CvResponseDTO(byte[] fileContent, String fileName) {
        this.fileContent = fileContent;
        this.fileName = fileName;
    }
}