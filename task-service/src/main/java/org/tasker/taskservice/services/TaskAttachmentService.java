package org.tasker.taskservice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.tasker.taskservice.api.dto.add.AddTaskAttachmentResponse;
import org.tasker.taskservice.api.dto.get.AttachmentDownloadResponse;
import org.tasker.taskservice.api.dto.get.GetAttachmentUrlResponse;

import java.util.List;

public interface TaskAttachmentService {

    AddTaskAttachmentResponse uploadAttachment(MultipartFile file, Long taskId);

    AttachmentDownloadResponse downloadAttachmentWithMetadata(Long attachmentId, Long taskId);

    GetAttachmentUrlResponse getDownloadUrl(Long attachmentId, Long taskId);

    String generateUploadUrl(Long taskId, String fileName, String contentType, Long fileSize);

    AddTaskAttachmentResponse completeUpload(Long taskId, String fileName, String s3Key);

    void deleteAttachment(Long attachmentId, Long taskId);

    List<GetAttachmentUrlResponse> getAllDownloadUrls(Long taskId, Pageable pageable);
}
