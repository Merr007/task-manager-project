package org.tasker.taskservice.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasker.taskservice.api.dto.*;
import org.tasker.taskservice.domain.TaskStatus;
import org.tasker.taskservice.mappers.TaskMapper;
import org.tasker.taskservice.service.TaskService;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final TaskMapper mapper;

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskCreateDto request) {
        var created = service.createTask(request);
        return ResponseEntity.created(URI.create("/api/v1/tasks/" + created.getId()))
                .body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public TaskResponseDto getTask(@PathVariable UUID id) {
        return mapper.toResponse(service.getTaskById(id));
    }

    @GetMapping
    public Page<TaskResponseDto> getTasksBySimpleFilter(@RequestParam(required = false) UUID projectId,
                                      @RequestParam(required = false) UUID assigneeId,
                                      @RequestParam(required = false) TaskStatus status,
                                      Pageable pageable) {
        return service.getTasksBySimpleFilter(projectId, assigneeId, status, pageable).map(mapper::toResponse);
    }

    @PatchMapping("/{id}")
    public TaskResponseDto updateTask(@PathVariable UUID id, @Valid @RequestBody TaskUpdateDto patch) {
        return mapper.toResponse(service.updateTask(id, patch));
    }

    @PostMapping("/{id}/assign")
    public TaskResponseDto assignTask(@PathVariable UUID id, @Valid @RequestBody AssignRequest req) {
        return mapper.toResponse(service.assignTask(id, req.getAssigneeId()));
    }

    @PostMapping("/{id}/status")
    public TaskResponseDto changeStatus(@PathVariable UUID id, @Valid @RequestBody StatusChangeRequest req) {
        return mapper.toResponse(service.changeStatus(id, req.getStatus()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
