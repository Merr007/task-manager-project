package org.tasker.projectservice.utils;

import org.springframework.web.multipart.MultipartFile;
import org.tasker.projectservice.data.entities.ProjectAttachment;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

public final class S3ProviderUtils {

    private S3ProviderUtils() {
    }

    public static GetObjectRequest getObjectRequest(String bucket, ProjectAttachment attachment) {
        return GetObjectRequest.builder()
                .bucket(bucket)
                .key(attachment.getFilePath())
                .build();
    }

    public static PutObjectRequest putObjectRequest(String bucket, String key, MultipartFile file) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();
    }

    public static DeleteObjectRequest deleteObjectRequest(String bucket, ProjectAttachment attachment) {
        return DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(attachment.getFilePath())
                .build();
    }

    public static GetObjectPresignRequest getObjectPresignRequest(GetObjectRequest getObjectRequest, long signatureDuration) {
        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(signatureDuration))
                .getObjectRequest(getObjectRequest)
                .build();
    }

    public static String generateKey(Long projectId, String fileName) {
        return projectId + "/" + UUID.randomUUID() + "_" + fileName;
    }
}
