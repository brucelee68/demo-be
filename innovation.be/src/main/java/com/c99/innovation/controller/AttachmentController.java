package com.c99.innovation.controller;

import com.c99.innovation.common.FileUtils;
import com.c99.innovation.dto.AttachmentDTO;
import com.c99.innovation.dto.ResponseDTO;
import com.c99.innovation.exception.UploadAttachmentException;
import com.c99.innovation.service.AWSS3Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

import static com.c99.innovation.common.constant.Constants.API_V1;
import static com.c99.innovation.common.constant.Constants.EMPTY_FILE_UPLOAD_MESSAGE;
import static com.c99.innovation.common.constant.Constants.UPLOAD_ATTACHMENT_SUCCESSFULLY;

@Api(value = "Innovation Controller")
@RestController
public class AttachmentController {

    private AWSS3Service awsS3Service;

    @Autowired
    public AttachmentController(AWSS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @ApiOperation(value = "Upload attachment which relate to innovation to the cloud storage")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPLOAD_ATTACHMENT_SUCCESSFULLY),
            @ApiResponse(code = 400, message = EMPTY_FILE_UPLOAD_MESSAGE)})
    @PostMapping(value = API_V1 + "/innovation" + "/attachment")
    public ResponseDTO<AttachmentDTO> uploadImage(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            AttachmentDTO attachment = this.awsS3Service.uploadFile(file);
            return new ResponseDTO<>(HttpStatus.OK, UPLOAD_ATTACHMENT_SUCCESSFULLY, attachment);
        }
        throw new UploadAttachmentException(EMPTY_FILE_UPLOAD_MESSAGE);
    }

    @ApiOperation(value = "Upload attachments which relate to innovation to the cloud storage")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = UPLOAD_ATTACHMENT_SUCCESSFULLY),
            @ApiResponse(code = 400, message = EMPTY_FILE_UPLOAD_MESSAGE)})
    @PostMapping(value = API_V1 + "/innovation" + "/attachments")
    public ResponseDTO<List<AttachmentDTO>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        List<File> listImages = FileUtils.convertAndValidateListImages(files);
        List<AttachmentDTO> attachmentDTOList = this.awsS3Service.uploadFile(listImages);
        return new ResponseDTO<>(HttpStatus.OK, UPLOAD_ATTACHMENT_SUCCESSFULLY, attachmentDTOList);
    }
}
