package com.example.SecureStorage.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@MappedSuperclass
public class BaseEntity {
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = true)
    private LocalDateTime updatedAt;

    protected BaseEntity() {
    }

    @PrePersist
    protected void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void setLastModifiedAt() {
        updatedAt = LocalDateTime.now();
    }
}
