package org.tasker.taskservice.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasker.taskservice.api.dto.create.CreateTaskCommentRequest;
import org.tasker.taskservice.api.dto.get.GetTaskCommentResponse;
import org.tasker.taskservice.services.TaskCommentsService;

import java.util.List;

@Tag(name = "Task comments operations management")
@RestController
@RequestMapping(path = "/api/v1/tasks/{taskId}/comments",
        produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TaskCommentController {

    private final TaskCommentsService taskCommentsService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create comment for task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment successfully created")
    })
    public void createComment(@Parameter(description = "Task identifier", required = true)
                              @PathVariable Long taskId,
                              @Parameter(description = "Create comment request")
                              @RequestBody @Valid CreateTaskCommentRequest request) {
        taskCommentsService.createComment(taskId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all comments for task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTaskCommentResponse.class)))
    })
    public ResponseEntity<List<GetTaskCommentResponse>> getComments(@Parameter(description = "Task identifier", required = true)
                                                                    @PathVariable Long taskId,
                                                                    @Parameter(description = "Page offset")
                                                                    @RequestParam(required = false, defaultValue = "0")
                                                                    int offset,
                                                                    @Parameter(description = "Size of page")
                                                                    @RequestParam(required = false, defaultValue = "10")
                                                                    int limit,
                                                                    @Parameter(description = "User identifier")
                                                                    @RequestParam(required = false)
                                                                    String userId) {
        Pageable pageable = PageRequest.of(offset, limit);
        return ResponseEntity.ok(taskCommentsService.getComments(taskId, pageable, userId));
    }

    @DeleteMapping("/{commentId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment successfully deleted")
    })
    public void deleteComment(@Parameter(description = "Task identifier", required = true)
                              @PathVariable Long taskId,
                              @Parameter(description = "Comment identifier", required = true)
                              @PathVariable Long commentId,
                              @Parameter(description = "User identifier", required = true)
                              @RequestHeader("X-User-Id") String userId) {
        taskCommentsService.deleteComment(taskId, commentId, userId);
    }
}
