package org.tasker.projectservice.api.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tasker.projectservice.api.dto.add.AddProjectMemberResponse;
import org.tasker.projectservice.api.dto.create.CreateProjectMemberRequest;
import org.tasker.projectservice.api.dto.delete.DeleteProjectMemberRequest;
import org.tasker.projectservice.api.dto.get.GetMemberResponse;
import org.tasker.projectservice.services.ProjectMembersService;

import java.util.List;

@RestController
@Tag(name = "Project members operations management")
@RequestMapping("/api/v1/projects/{projectId}/members")
@AllArgsConstructor
public class ProjectMembersController {

    private final ProjectMembersService projectMembersService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AddProjectMemberResponse> addProjectMember(@PathVariable("projectId") Long projectId,
                                                                     @Valid @RequestBody CreateProjectMemberRequest request) {
        return ResponseEntity.ok(projectMembersService.addProjectMember(request, projectId));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectMember(@PathVariable("projectId") Long projectId,
                                    @Valid @RequestBody DeleteProjectMemberRequest request) {
        projectMembersService.removeProjectMember(projectId, request.memberId());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<GetMemberResponse>> getAllMembers(@PathVariable("projectId") Long projectId,
                                                                 @RequestParam(value = "limit", defaultValue = "10") int limit,
                                                                 @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return ResponseEntity.ok(projectMembersService.getAllProjectMembers(projectId, limit, offset));
    }
}
