package com.example.SecureStorage.domain.controller;

import java.util.Set;

import org.checkerframework.checker.units.qual.A;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.SecureStorage.domain.controller.StorageItemControllerKit.StorageItemResult;

@Controller
public class StorageItemController {

    @MutationMapping
    public String uploadDocument() {
        return "Not implemented yet";
    }

    @QueryMapping
    public Set<StorageItemResult> listStorageItems(@Argument Long sectionId) {
        return new HashSet<>();
    } 

    @QueryMapping
    public StorageItemResult getStorageItemDetail(@Argument Long id) {

        return null;
    }
}
