package org.tasker.taskservice.api.dto.get;

public record GetAttachmentUrlResponse(
        Long id,
        String fileName,
        String url
) {
}
