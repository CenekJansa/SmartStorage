package com.example.SecureStorage.messaging;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentProcessingMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private byte[] fileData;
    private Long sectionId;
    private Long attachmentId;
    private String fileName;
}

