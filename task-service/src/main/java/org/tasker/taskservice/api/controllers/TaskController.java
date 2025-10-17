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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasker.common.validation.SortOrder;
import org.tasker.taskservice.api.dto.add.AddAssigneeRequest;
import org.tasker.taskservice.api.dto.create.CreateTaskRequest;
import org.tasker.taskservice.api.dto.create.CreateTaskResponse;
import org.tasker.taskservice.api.dto.get.GetTaskResponse;
import org.tasker.taskservice.api.dto.update.UpdateTaskRequest;
import org.tasker.taskservice.api.dto.update.UpdateTaskResponse;
import org.tasker.taskservice.services.TaskService;
import org.tasker.taskservice.validation.TaskSortBy;

import java.util.List;

@Tag(name = "Task operations management")
@RestController
@RequestMapping(path = "/api/v1/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateTaskResponse.class))
            )
    })
    public ResponseEntity<CreateTaskResponse> createTask(@Parameter(description = "Create task request")
                                                         @RequestBody @Valid CreateTaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(request));
    }

    @PostMapping("/{taskId}/assign")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Assign task to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task successfully assigned")
    })
    public void addAssignee(@Parameter(description = "Task identifier", required = true)
                            @PathVariable Long taskId,
                            @Parameter(description = "Add task's assignee")
                            @RequestBody @Valid AddAssigneeRequest request) {
        taskService.addAssignee(taskId, request);
    }

    @DeleteMapping("/{taskId}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted")
    })
    public void deleteTask(@Parameter(description = "Task identifier", required = true)
                           @PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

    @GetMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get task by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTaskResponse.class)))
    })
    public ResponseEntity<GetTaskResponse> getTask(@Parameter(description = "Task identifier", required = true)
                                                   @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @GetMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all tasks for project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetTaskResponse.class)))
    })
    public ResponseEntity<List<GetTaskResponse>> getAllTasks(@Parameter(description = "Project identifier", required = true)
                                                             @PathVariable Long projectId,
                                                             @Parameter(description = "Page offset")
                                                             @RequestParam(required = false, defaultValue = "0")
                                                             int offset,
                                                             @Parameter(description = "Size of page")
                                                             @RequestParam(required = false, defaultValue = "10")
                                                             int limit,
                                                             @Parameter(description = "Sorting field")
                                                             @RequestParam(required = false)
                                                             String sortBy,
                                                             @Parameter(description = "Sorting order (asc or desc)")
                                                             @RequestParam(required = false, defaultValue = "asc")
                                                             String sortOrder,
                                                             @Parameter(description = "Partial task title for searching")
                                                             @RequestParam(required = false)
                                                             String taskTitle) {
        Sort sort = null;
        TaskSortBy taskSortBy = TaskSortBy.fromString(sortBy);
        SortOrder order = SortOrder.fromString(sortOrder);
        if (taskSortBy != null) {
            Sort.Direction direction = (order == SortOrder.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, taskSortBy.getSortBy());
        }
        Pageable pageable = sort != null ? PageRequest.of(offset, limit, sort) : PageRequest.of(offset, limit);
        return ResponseEntity.ok(taskService.getAllTasks(pageable, projectId, taskTitle));
    }

    @PutMapping("/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateTaskResponse.class)))
    })
    public ResponseEntity<UpdateTaskResponse> updateTask(@Parameter(description = "Task identifier", required = true)
                                                         @PathVariable Long taskId,
                                                         @Parameter(description = "Update task request")
                                                         @RequestBody @Valid UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }
}
