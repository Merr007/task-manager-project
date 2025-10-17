package org.tasker.projectservice.api.dto.get;

public record GetAttachmentUrlResponse(
        Long id,
        String fileName,
        String url,
        long fileSize
) {
}
