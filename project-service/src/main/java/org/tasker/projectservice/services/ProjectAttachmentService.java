package org.tasker.projectservice.services;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.tasker.projectservice.api.dto.add.AddProjectAttachmentResponse;
import org.tasker.projectservice.api.dto.get.AttachmentDownloadResponse;
import org.tasker.projectservice.api.dto.get.GetAttachmentUrlResponse;

import java.util.List;

public interface ProjectAttachmentService {

    AddProjectAttachmentResponse uploadAttachment(MultipartFile file, Long projectId);

    AttachmentDownloadResponse downloadAttachmentWithMetadata(Long attachmentId, Long projectId);

    GetAttachmentUrlResponse getDownloadUrl(Long attachmentId, Long projectId);

    String generateUploadUrl(Long projectId, String fileName, String contentType, Long fileSize);

    AddProjectAttachmentResponse completeUpload(Long projectId, String fileName, String s3Key);

    void deleteAttachment(Long attachmentId, Long projectId);

    List<GetAttachmentUrlResponse> getAllDownloadUrls(Long projectId, Pageable pageable);
}
