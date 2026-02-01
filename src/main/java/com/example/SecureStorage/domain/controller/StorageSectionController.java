package com.example.SecureStorage.domain.controller;

import java.util.Map;

import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.example.SecureStorage.domain.controller.StorageSectionControllerKit.StorageSectionResult;

@Controller
public class StorageSectionController {
    @

    // create new section
    @MutationMapping
    public StorageSectionResult createStorageSection(String name, Map<String, String> attributes) {
        return null;
    }

    // get sections

    // get one section

    // update section

}
