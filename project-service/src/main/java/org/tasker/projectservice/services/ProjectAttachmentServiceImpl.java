package org.tasker.projectservice.services;

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
import org.tasker.projectservice.api.dto.add.AddProjectAttachmentResponse;
import org.tasker.projectservice.api.dto.get.AttachmentDownloadResponse;
import org.tasker.projectservice.api.dto.get.GetAttachmentUrlResponse;
import org.tasker.projectservice.data.entities.Project;
import org.tasker.projectservice.data.entities.ProjectAttachment;
import org.tasker.projectservice.data.repositories.ProjectAttachmentRepository;
import org.tasker.projectservice.data.repositories.ProjectRepository;
import org.tasker.projectservice.exception.NoSuchProjectException;
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
public class ProjectAttachmentServiceImpl implements ProjectAttachmentService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final ProjectAttachmentRepository attachmentRepository;
    private final ProjectRepository projectRepository;
    private final AwsProperties awsProperties;

    @Override
    @Transactional
    public AddProjectAttachmentResponse uploadAttachment(MultipartFile file, Long projectId) throws S3UploadException {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchProjectException("Project not found"));

        String fileName = file.getOriginalFilename();
        String key = S3ProviderUtils.generateKey(projectId, fileName);

        try {
            PutObjectRequest putObjectRequest = S3ProviderUtils.putObjectRequest(awsProperties.getS3().getBucket(), key, file);

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            ProjectAttachment attachment = new ProjectAttachment();
            attachment.setProject(project);
            attachment.setFilename(fileName);
            attachment.setFilesize(file.getSize());
            attachment.setContentType(file.getContentType());
            attachment.setUploadDate(LocalDateTime.now());
            attachment.setFilePath(key);

            ProjectAttachment saved = attachmentRepository.save(attachment);
            log.info("Attachment saved to s3: {}", saved.getFilename());
            return new AddProjectAttachmentResponse(saved.getId());
        } catch (IOException e) {
            log.error("Error uploading file to MinIO", e);
            throw new S3UploadException("Failed to upload attachment");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AttachmentDownloadResponse downloadAttachmentWithMetadata(Long attachmentId, Long projectId) throws S3ObjectNotFoundException {
        ProjectAttachment attachment = getProjectAttachmentOrElseThrow(attachmentId, projectId);

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
            throw new S3ObjectNotFoundException("Не удалось скачать файл: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GetAttachmentUrlResponse getDownloadUrl(Long attachmentId, Long projectId) throws S3ObjectNotFoundException {
        ProjectAttachment attachment = getProjectAttachmentOrElseThrow(attachmentId, projectId);

        try {
            String url = getObjectPresignedUrl(attachment);

            log.info("Generated download URL for attachment: {}", attachment.getFilename());
            return new GetAttachmentUrlResponse(attachment.getId(), attachment.getFilename(), url, attachment.getFilesize());
        } catch (Exception e) {
            log.error("Error generating download URL for attachment {}: {}", attachment.getFilename(), e.getMessage(), e);
            throw new S3ObjectNotFoundException("Не удалось сгенерировать URL для скачивания файла: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String generateUploadUrl(Long projectId, String fileName, String contentType, Long fileSize) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchProjectException("Project not found"));

        String key = S3ProviderUtils.generateKey(projectId, fileName);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(awsProperties.getS3().getBucket())
                .key(key)
                .contentType(contentType)
                .contentLength(fileSize)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(awsProperties.getS3().getSignatureDuration()))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
        String url = presignedRequest.url().toString();

        log.info("Generated upload URL for file: {} in project: {}", fileName, projectId);
        return url;
    }

    @Override
    @Transactional
    public AddProjectAttachmentResponse completeUpload(Long projectId, String fileName, String s3Key) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchProjectException("Project not found"));

        ProjectAttachment attachment = new ProjectAttachment();
        attachment.setProject(project);
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

        ProjectAttachment saved = attachmentRepository.save(attachment);
        log.info("Completed upload for file: {} in project: {}", fileName, projectId);

        return new AddProjectAttachmentResponse(saved.getId());
    }

    @Override
    @Transactional
    public void deleteAttachment(Long attachmentId, Long projectId) throws S3ObjectNotFoundException {
        ProjectAttachment attachment = getProjectAttachmentOrElseThrow(attachmentId, projectId);

        DeleteObjectRequest deleteObjectRequest = S3ProviderUtils.deleteObjectRequest(awsProperties.getS3().getBucket(), attachment.getFilePath());

        try {
            s3Client.deleteObject(deleteObjectRequest);
            log.info("Deleted attachment from S3: {}", attachment.getFilename());
        } catch (Exception e) {
            log.error("Failed to delete attachment from S3: {}", attachment.getFilename(), e);
            throw new S3ObjectNotFoundException("Failed to delete attachment from S3: " + e.getMessage());
        }

        int deletedRows = attachmentRepository.deleteByProjectIdAndId(projectId, attachmentId);
        log.info("Deleted {} rows from database for attachment: {}", deletedRows, attachment.getFilename());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetAttachmentUrlResponse> getAllDownloadUrls(Long projectId, Pageable pageable) {
        projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchProjectException("Project not found"));

        List<ProjectAttachment> attachments = attachmentRepository.findByProjectId(projectId, pageable);

        List<GetAttachmentUrlResponse> urls = attachments.stream()
                .map(attachment -> {
                    try {
                        return new GetAttachmentUrlResponse(attachment.getId(), attachment.getFilename(), getObjectPresignedUrl(attachment), attachment.getFilesize());
                    } catch (Exception e) {
                        log.error("Failed to generate download URL for attachment: {}", attachment.getFilename(), e);
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .toList();

        log.info("Generated {} download URLs for project: {} with limit: {} offset: {}",
                urls.size(), projectId, pageable.getPageSize(), pageable.getOffset());
        return urls;
    }

    private String getObjectPresignedUrl(ProjectAttachment attachment) {
        GetObjectRequest getObjectRequest = S3ProviderUtils.getObjectRequest(awsProperties.getS3().getBucket(), attachment.getFilePath());

        GetObjectPresignRequest presignRequest = S3ProviderUtils.getObjectPresignRequest(
                getObjectRequest,
                awsProperties.getS3().getSignatureDuration());

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    private ProjectAttachment getProjectAttachmentOrElseThrow(Long attachmentId, Long projectId) throws NoSuchProjectException, S3ObjectNotFoundException {
        ProjectAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new S3ObjectNotFoundException("Attachment not found"));

        boolean existsInProject = projectRepository.getProjectWithAttachments(projectId)
                .orElseThrow(() -> new NoSuchProjectException("Project not found"))
                .getAttachments().contains(attachment);

        if (!existsInProject) {
            throw new S3ObjectNotFoundException("Attachment not found in project with id " + projectId);
        }

        return attachment;
    }
}
