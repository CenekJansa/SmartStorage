package com.example.SecureStorage.domain.controller;

import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.domain.controller.StorageItemControllerKit.StorageItemResult;
import com.example.SecureStorage.domain.controller.StorageItemControllerKit.UploadResult;
import com.example.SecureStorage.domain.service.StorageItemService;

@Controller
public class StorageItemController {

    @Autowired
    private StorageItemService storageItemService;

    @MutationMapping
    public UploadResult uploadDocument(@Argument Long sectionId, @Argument MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            byte[] fileData = file.getBytes();

            OperationResult<Long> result
             = storageItemService.uploadFile(sectionId, fileName, fileData);

            UploadResult uploadResult = new UploadResult();
            uploadResult.setSuccess(result.isSuccess());

            if (result.isSuccess()) {
                uploadResult.setAttachmentId(result.getData());
            } else {
                uploadResult.setErrorMessage(result.getErrorMessage());
            }

            return uploadResult;
        } catch (IOException e) {
            UploadResult errorResult = new UploadResult();
            errorResult.setSuccess(false);
            errorResult.setErrorMessage("Failed to read file: " + e.getMessage());
            return errorResult;
        }
    }

    @QueryMapping
    public Set<StorageItemResult> listStorageItems(@Argument Long sectionId) {
        return null;
    } 

    @QueryMapping
    public StorageItemResult getStorageItemDetail(@Argument Long id) {
        
        return null;
    }
}
