package com.example.SecureStorage.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageSectionField {
    private String name;
    private FieldType type;
    // explicit JsonProperty needed: Lombok generates isIdentifier() which Jackson maps to "identifier" by default
    @JsonProperty("isIdentifier")
    private boolean isIdentifier;
}
