package com.example.SecureStorage.domain.service;

import javax.validation.constraints.NotNull;

import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionInputVo;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVo;

public interface StorageSectionService {

    StorageSectionResVo createStorageSection(@NotNull StorageSectionInputVo inputVo);

    // TODO rest of the methods
}
