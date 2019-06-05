package com.gmail.derynem.service.model;

import com.gmail.derynem.service.validation.annotation.ValidFile;
import org.springframework.web.multipart.MultipartFile;

public class UploadDTO {
    @ValidFile
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}