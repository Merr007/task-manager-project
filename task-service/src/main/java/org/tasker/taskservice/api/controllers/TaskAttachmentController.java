package org.tasker.taskservice.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tasker.taskservice.api.dto.add.AddTaskAttachmentResponse;
import org.tasker.taskservice.api.dto.get.AttachmentDownloadResponse;
import org.tasker.taskservice.api.dto.get.GetAttachmentUrlResponse;
import org.tasker.taskservice.services.TaskAttachmentService;

import java.util.List;

@RestController
@Tag(name = "Task attachment operations management")
@RequestMapping("/api/v1/tasks/{taskId}")
@RequiredArgsConstructor
public class TaskAttachmentController {

    private final TaskAttachmentService attachmentService;

    @PostMapping(value = "/attachments/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload attachment to task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attachment uploaded successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<AddTaskAttachmentResponse> uploadAttachment(@Parameter(description = "Task identifier", required = true)
                                                                      @PathVariable Long taskId,
                                                                      @Parameter(description = "File to upload")
                                                                      @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attachmentService.uploadAttachment(file, taskId));
    }

    @GetMapping("/attachments/{attachmentId}/download")
    @Operation(summary = "Download attachment")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment downloaded successfully"),
            @ApiResponse(responseCode = "404", description = "Attachment not found")
    })
    public ResponseEntity<InputStreamResource> downloadAttachment(@Parameter(description = "Task identifier", required = true)
                                                                  @PathVariable Long taskId,
                                                                  @Parameter(description = "Attachment identifier", required = true)
                                                                  @PathVariable Long attachmentId) {
        AttachmentDownloadResponse downloadData = attachmentService.downloadAttachmentWithMetadata(attachmentId, taskId);
        var resource = new InputStreamResource(downloadData.inputStream());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadData.fileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, downloadData.contentType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(downloadData.fileSize()))
                .body(resource);
    }

    @GetMapping("/attachments/{attachmentId}/url")
    @Operation(summary = "Get download URL for attachment")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Download URL generated successfully"),
            @ApiResponse(responseCode = "404", description = "Attachment not found")
    })
    public ResponseEntity<GetAttachmentUrlResponse> getDownloadUrl(@Parameter(description = "Task identifier", required = true)
                                                                   @PathVariable Long taskId,
                                                                   @Parameter(description = "Attachment identifier", required = true)
                                                                   @PathVariable Long attachmentId) {
        return ResponseEntity.status(HttpStatus.OK).body(attachmentService.getDownloadUrl(attachmentId, taskId));
    }

    @GetMapping("/attachments")
    @Operation(summary = "Get a batch of attachment URLs")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Download URLs generated successfully"),
    })
    public ResponseEntity<List<GetAttachmentUrlResponse>> getAllDownloadUrls(@Parameter(description = "Task identifier", required = true)
                                                                             @PathVariable Long taskId,
                                                                             @Parameter(description = "Size of page")
                                                                             @RequestParam(required = false, defaultValue = "10")
                                                                             int limit,
                                                                             @Parameter(description = "Page offset")
                                                                             @RequestParam(required = false, defaultValue = "0")
                                                                             int offset) {
        return ResponseEntity.ok(attachmentService.getAllDownloadUrls(taskId, PageRequest.of(offset, limit)));
    }

    @PostMapping("/attachments/upload-url")
    @Operation(summary = "Generate upload URL for direct file upload")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload URL generated successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<String> getUploadUrl(@Parameter(description = "Task identifier", required = true)
                                               @PathVariable Long taskId,
                                               @Parameter(description = "File name")
                                               @RequestParam String fileName,
                                               @Parameter(description = "Content type")
                                               @RequestParam String contentType,
                                               @Parameter(description = "File size in bytes")
                                               @RequestParam Long fileSize) {
        String uploadUrl = attachmentService.generateUploadUrl(taskId, fileName, contentType, fileSize);
        return ResponseEntity.ok(uploadUrl);
    }

    @PostMapping("/attachments/complete-upload")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Complete file upload and save metadata")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Upload completed successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public ResponseEntity<AddTaskAttachmentResponse> completeUpload(@Parameter(description = "Task identifier", required = true)
                                                                    @PathVariable Long taskId,
                                                                    @Parameter(description = "File name")
                                                                    @RequestParam String fileName,
                                                                    @Parameter(description = "S3 object key")
                                                                    @RequestParam String s3Key) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attachmentService.completeUpload(taskId, fileName, s3Key));
    }

    @DeleteMapping("/attachments/{attachmentId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete attachment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Attachment not found")
    })
    public void deleteAttachment(@Parameter(description = "Task identifier", required = true)
                                 @PathVariable Long taskId,
                                 @Parameter(description = "Attachment identifier", required = true)
                                 @PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId, taskId);
    }
}
