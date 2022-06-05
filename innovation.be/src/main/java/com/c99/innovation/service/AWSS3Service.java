package com.c99.innovation.service;

import com.c99.innovation.dto.AttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface AWSS3Service {
    AttachmentDTO uploadFile(MultipartFile multipartFile);

    List<AttachmentDTO> uploadFile(List<File> files);

    void deleteFileFromS3Bucket(String fileName);
}
