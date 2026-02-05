package com.example.SecureStorage.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.SecureStorage.domain.entity.StorageItem;

public interface StorageItemRepository extends JpaRepository<StorageItem, Long> {

    List<StorageItem> findByStorageSectionId(Long sectionId);

    /**
     * Find a StorageItem by a specific metadata attribute value
     * Uses JSONB operators to search within the metadata_json column
     *
     * @param sectionId the storage section ID
     * @param attributeKey the metadata key to search for (e.g., "VIN")
     * @param attributeValue the value to match
     * @return Optional containing the first matching item, or empty if not found
     */
    @Query(value = "SELECT * FROM storage_item " +
                   "WHERE storage_section_id = :sectionId " +
                   "AND (metadata_json->>CAST(:attributeKey AS TEXT)) = CAST(:attributeValue AS TEXT) " +
                   "LIMIT 1",
           nativeQuery = true)
    Optional<StorageItem> findByMetadataAttribute(
        @Param("sectionId") Long sectionId,
        @Param("attributeKey") String attributeKey,
        @Param("attributeValue") String attributeValue
    );

}
