package org.tasker.taskservice.api.dto.get;

import java.io.InputStream;

public record AttachmentDownloadResponse(
        InputStream inputStream,
        String fileName,
        String contentType,
        Long fileSize) {
}
