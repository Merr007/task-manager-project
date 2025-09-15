package org.tasker.projectservice.api.controllers;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.tasker.projectservice.api.dto.add.AddProjectTagsRequest;
import org.tasker.projectservice.api.dto.delete.DeleteProjectTagRequest;
import org.tasker.projectservice.services.ProjectService;

@RestController
@Tag(name = "Project tags operations management")
@RequestMapping(path = "/v1/projects/{projectId}/tags", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProjectTagController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void addProjectTag(@Parameter(description = "Project identifier", required = true)
                              @PathVariable("projectId") Long projectId,
                              @RequestBody @Valid AddProjectTagsRequest request) {
        projectService.addProjectTags(projectId, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteProjectTag(@Parameter(description = "Project identifier", required = true)
                                 @PathVariable("projectId") Long projectId,
                                 @Parameter(description = "Full deletion including tag repository")
                                 @RequestParam(value = "full", defaultValue = "false", required = false) boolean isFull,
                                 @RequestBody @Valid DeleteProjectTagRequest request) {
        projectService.deleteProjectTags(projectId, request.name(), isFull);
    }
}
