package org.tasker.projectservice.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.tasker.common.api.dto.user.GetUserResponse;
import org.tasker.common.client.UsersRestClient;
import org.tasker.common.exception.RestException;
import org.tasker.common.validation.SortOrder;
import org.tasker.projectservice.api.dto.create.CreateProjectRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectResponse;
import org.tasker.projectservice.api.dto.get.GetProjectResponse;
import org.tasker.projectservice.exception.NoSuchProjectException;
import org.tasker.projectservice.services.ProjectService;
import org.tasker.projectservice.validation.ProjSortBy;

import java.util.List;

@Tag(name = "Project operations management")
@RestController
@RequestMapping(path = "/v1/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final UsersRestClient usersRestClient;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Project successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateProjectResponse.class))),
    })
    public ResponseEntity<CreateProjectResponse> createProject(@Parameter(description = "Create project request")
                                           @RequestBody @Valid CreateProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get project info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get specified project",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectResponse.class))),
            @ApiResponse(responseCode = "404", description = "Project not found by specified Id")
    })
    public ResponseEntity<GetProjectResponse> getProject(@Parameter(description = "Project identifier", required = true)
                                        @PathVariable Long projectId) {
        try {
            return ResponseEntity.ok().body(projectService.getProject(projectId));
        } catch (NoSuchProjectException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete project with id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful project delete"),
            @ApiResponse(responseCode = "404", description = "Project not found by specified Id")
    })
    public void deleteProject(@Parameter(description = "Project identifier", required = true)
                              @PathVariable Long projectId) {
        try {
            projectService.deleteProject(projectId);
        } catch (NoSuchProjectException e) {
            throw new RestException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully get all projects",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetProjectResponse.class)))
    })
    public ResponseEntity<List<GetProjectResponse>> getAllProjects(
            @Parameter(description = "Page offset")
            @RequestParam(required = false, defaultValue = "0")
            int offset,
            @Parameter(description = "Size of page")
            @RequestParam(required = false, defaultValue = "10")
            int limit,
            @Parameter(description = "Sorting field",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "string",
                            allowableValues = {"name", "startDate", "endDate", "status"}))
            @RequestParam(required = false)
            String sortBy,
            @Parameter(description = "Sorting order (asc or desc)",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "string",
                            allowableValues = {"asc", "desc"}))
            @RequestParam(required = false, defaultValue = "asc")
            String sortOrder) {
        Sort sort = null;
        ProjSortBy projSortBy = ProjSortBy.fromString(sortBy);
        SortOrder order = SortOrder.fromString(sortOrder);
        if (projSortBy != null) {
            Sort.Direction direction = (order == SortOrder.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, projSortBy.getSortBy());
        }
        Pageable pageable = sort != null ? PageRequest.of(offset, limit, sort) : PageRequest.of(offset, limit);
        return ResponseEntity.ok(projectService.getAllProjects(pageable));
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("username") String username) {
        System.out.println("Authentication: " + ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getToken().getTokenValue());
        return ResponseEntity.ok(usersRestClient.getUserByUsername(username).get());
    }
}
