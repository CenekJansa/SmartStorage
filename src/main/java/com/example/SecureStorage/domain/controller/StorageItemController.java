package com.example.SecureStorage.domain.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.domain.controller.StorageItemControllerKit.StorageItemResult;
import com.example.SecureStorage.domain.controller.StorageItemControllerKit.StorageItemResultMapper;
import com.example.SecureStorage.domain.controller.StorageItemControllerKit.UploadResult;
import com.example.SecureStorage.domain.service.StorageItemService;
import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemResultVo;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StorageItemController {

    @Autowired
    private StorageItemService storageItemService;;

    @MutationMapping
    public UploadResult uploadDocument(@Argument @NotNull Long sectionId, @Argument MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());
        try {
            String fileName = file.getOriginalFilename();
            if (fileName == null || fileName.isEmpty()) {
                UploadResult errorResult = new UploadResult();
                errorResult.setSuccess(false);
                errorResult.setErrorMessage("File name is missing");
                return errorResult;
            }
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
    public List<StorageItemResult> retrieveStorageItems(@Argument @NotNull Long sectionId) {
        log.info("Retrieving storage items for sectionId: {}", sectionId);
        List<StorageItemResultVo> resultVos = storageItemService.retrieveStorageItems(sectionId);
        return resultVos.stream()
            .map(StorageItemResultMapper::mapFrom)
            .toList();
    } 

    @QueryMapping
    public StorageItemResult getStorageItemDetail(@Argument Long id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
