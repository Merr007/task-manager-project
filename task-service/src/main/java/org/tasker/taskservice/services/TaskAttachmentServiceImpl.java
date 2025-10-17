package org.tasker.taskservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tasker.common.s3.exception.S3ObjectNotFoundException;
import org.tasker.common.s3.exception.S3UploadException;
import org.tasker.common.s3.properties.AwsProperties;
import org.tasker.common.s3.utils.S3ProviderUtils;
import org.tasker.taskservice.api.dto.add.AddTaskAttachmentResponse;
import org.tasker.taskservice.api.dto.get.AttachmentDownloadResponse;
import org.tasker.taskservice.api.dto.get.GetAttachmentUrlResponse;
import org.tasker.taskservice.data.entities.Task;
import org.tasker.taskservice.data.entities.TaskAttachment;
import org.tasker.taskservice.data.repositories.TaskAttachmentRepository;
import org.tasker.taskservice.data.repositories.TaskRepository;
import org.tasker.taskservice.exception.NoSuchTaskException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final TaskAttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final AwsProperties awsProperties;

    @Override
    @Transactional
    public AddTaskAttachmentResponse uploadAttachment(MultipartFile file, Long taskId) throws S3UploadException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskException("Task not found"));

        String fileName = file.getOriginalFilename();
        String key = S3ProviderUtils.generateKey(taskId, fileName);

        try {
            PutObjectRequest putObjectRequest = S3ProviderUtils.putObjectRequest(awsProperties.getS3().getBucket(), key, file);

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            TaskAttachment attachment = new TaskAttachment();
            attachment.setTask(task);
            attachment.setFilename(fileName);
            attachment.setFilesize(file.getSize());
            attachment.setContentType(file.getContentType());
            attachment.setUploadDate(LocalDateTime.now());
            attachment.setFilePath(key);

            TaskAttachment saved = attachmentRepository.save(attachment);
            log.info("Attachment saved to s3: {}", saved.getFilename());
            return new AddTaskAttachmentResponse(saved.getId());
        } catch (IOException e) {
            log.error("Error uploading file to MinIO", e);
            throw new S3UploadException("Failed to upload attachment");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AttachmentDownloadResponse downloadAttachmentWithMetadata(Long attachmentId, Long taskId) throws S3ObjectNotFoundException {
        TaskAttachment attachment = getTaskAttachmentOrElseThrow(attachmentId, taskId);

        try {
            GetObjectRequest getObjectRequest = S3ProviderUtils.getObjectRequest(awsProperties.getS3().getBucket(), attachment.getFilePath());

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);

            log.info("Downloading attachment with metadata from s3: {}", attachment.getFilename());

            return new AttachmentDownloadResponse(
                    s3Object,
                    attachment.getFilename(),
                    attachment.getContentType() != null ? attachment.getContentType() : "application/octet-stream",
                    attachment.getFilesize()
            );
        } catch (Exception e) {
            log.error("Error downloading attachment {}: {}", attachment.getFilename(), e.getMessage(), e);
            throw new S3ObjectNotFoundException("Failed to download attachment " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GetAttachmentUrlResponse getDownloadUrl(Long attachmentId, Long taskId) throws S3ObjectNotFoundException {
        TaskAttachment attachment = getTaskAttachmentOrElseThrow(attachmentId, taskId);

        try {
            String url = getObjectPresignedUrl(attachment);

            log.info("Generated download URL for attachment: {}", attachment.getFilename());
            return new GetAttachmentUrlResponse(attachment.getId(), attachment.getFilename(), url);
        } catch (Exception e) {
            log.error("Error generating download URL for attachment {}: {}", attachment.getFilename(), e.getMessage(), e);
            throw new S3ObjectNotFoundException("Failed to generate download URL for attachment: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String generateUploadUrl(Long taskId, String fileName, String contentType, Long fileSize) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskException("Task not found"));

        String key = S3ProviderUtils.generateKey(taskId, fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(key)
                .contentType(contentType)
                .contentLength(fileSize)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String url = presignedRequest.url().toString();

        log.info("Generated upload URL for file: {} in task: {}", fileName, taskId);
        return url;
    }

    @Override
    @Transactional
    public AddTaskAttachmentResponse completeUpload(Long taskId, String fileName, String s3Key) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskException("Task not found"));

        TaskAttachment attachment = new TaskAttachment();
        attachment.setTask(task);
        attachment.setFilename(fileName);
        attachment.setFilePath(s3Key);
        attachment.setUploadDate(LocalDateTime.now());

        try {
            var headObject = s3Client.headObject(builder -> builder
                    .bucket(awsProperties.getS3().getBucket())
                    .key(s3Key)
                    .build());

            attachment.setFilesize(headObject.contentLength());
            attachment.setContentType(headObject.contentType());
        } catch (Exception e) {
            log.warn("Could not get file metadata from S3: {}", e.getMessage());
            attachment.setFilesize(0L);
            attachment.setContentType("application/octet-stream");
        }

        TaskAttachment saved = attachmentRepository.save(attachment);
        log.info("Completed upload for file: {} in task: {}", fileName, taskId);

        return new AddTaskAttachmentResponse(saved.getId());
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId, Long taskId) throws S3ObjectNotFoundException {
        TaskAttachment attachment = getTaskAttachmentOrElseThrow(attachmentId, taskId);

        DeleteObjectRequest deleteObjectRequest = S3ProviderUtils.deleteObjectRequest(awsProperties.getS3().getBucket(), attachment.getFilePath());

        try {
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Deleted attachment from S3: {}", attachment.getFilename());
        } catch (Exception e) {
            log.error("Failed to delete attachment from S3: {}", attachment.getFilename(), e);
            throw new S3ObjectNotFoundException("Failed to delete attachment from S3: " + e.getMessage());
        }

        int deletedRows = attachmentRepository.deleteByTaskIdAndId(taskId, attachmentId);
        log.info("Deleted {} rows from database for attachment: {}", deletedRows, attachment.getFilename());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAttachmentUrlResponse> getAllDownloadUrls(Long taskId, Pageable pageable) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskException("Task not found"));

        List<TaskAttachment> attachments = attachmentRepository.findByTaskId(taskId, pageable);

        List<GetAttachmentUrlResponse> urls = attachments.stream()
                .map(attachment -> {
                    try {
                        return new GetAttachmentUrlResponse(attachment.getId(), attachment.getFilename(), getObjectPresignedUrl(attachment));
                    } catch (Exception e) {
                        log.error("Failed to generate download URL for attachment: {}", attachment.getFilename(), e);
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .toList();

        log.info("Generated {} download URLs for task: {} with limit: {} offset: {}",
                urls.size(), taskId, pageable.getPageSize(), pageable.getOffset());
        return urls;
    }

    private String getObjectPresignedUrl(TaskAttachment attachment) {
        GetObjectRequest getObjectRequest = S3ProviderUtils.getObjectRequest(awsProperties.getS3().getBucket(), attachment.getFilePath());

        GetObjectPresignRequest presignRequest = S3ProviderUtils.getObjectPresignRequest(
                getObjectRequest,
                awsProperties.getS3().getSignatureDuration());

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private TaskAttachment getTaskAttachmentOrElseThrow(Long attachmentId, Long taskId) throws NoSuchTaskException, S3ObjectNotFoundException {
        TaskAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new S3ObjectNotFoundException("Attachment not found"));

        boolean existsInTask = taskRepository.getTaskWithAttachments(taskId)
                .orElseThrow(() -> new NoSuchTaskException("Task not found"))
                .getAttachments().contains(attachment);

        if (!existsInTask) {
            throw new S3ObjectNotFoundException("Attachment not found in task with id " + taskId);
        }

        return attachment;
    }
}
