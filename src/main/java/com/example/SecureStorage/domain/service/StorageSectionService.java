package com.example.SecureStorage.domain.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionInputVo;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVo;

public interface StorageSectionService {

    StorageSectionResVo createStorageSection(@NotNull StorageSectionInputVo inputVo);

    List<StorageSectionResVo> retrieveStorageSections();

    void removeStorageSection(@NotNull Long Id);

    StorageSectionResVo updateStorageSection(@NotNull Long Id, @NotNull StorageSectionInputVo inputVo);
}
