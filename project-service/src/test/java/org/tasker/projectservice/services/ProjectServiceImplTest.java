package org.tasker.projectservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.tasker.projectservice.api.dto.get.GetProjectResponse;
import org.tasker.projectservice.api.dto.update.UpdateProjectRequest;
import org.tasker.projectservice.api.dto.update.UpdateProjectResponse;
import org.tasker.projectservice.api.dto.update.UpdateStatusRequest;
import org.tasker.projectservice.api.dto.update.UpdateStatusResponse;
import org.tasker.projectservice.data.entities.Project;
import org.tasker.projectservice.data.repositories.ProjectRepository;
import org.tasker.projectservice.data.repositories.ProjectTagRepository;
import org.tasker.projectservice.domain.model.ProjectStatus;
import org.tasker.projectservice.exception.NoSuchProjectException;
import org.tasker.projectservice.exception.NoSuchTagException;
import org.tasker.projectservice.mappers.ProjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private ProjectTagRepository projectTagRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;
    private UpdateProjectRequest updateProjectRequest;
    private UpdateStatusRequest updateStatusRequest;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStartDate(LocalDate.of(2024, 1, 1));
        project.setEndDate(LocalDate.of(2024, 12, 31));
        project.setStatus(ProjectStatus.CREATED);


        updateProjectRequest = new UpdateProjectRequest(
                "Updated Project",
                "Updated Description",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 11, 30)
        );

        updateStatusRequest = new UpdateStatusRequest(ProjectStatus.IN_PROGRESS);
    }

    @Test
    void testGetProject() {
        // Given
        GetProjectResponse expectedResponse = new GetProjectResponse(1L, "Test Project", "Test Description",
                ProjectStatus.CREATED, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null);

        when(projectRepository.getProjectById(1L)).thenReturn(Optional.of(project));
        when(projectMapper.toGetProjectResponse(project)).thenReturn(expectedResponse);

        // When
        GetProjectResponse response = projectService.getProject(1L);

        // Then
        assertNotNull(response);
        assertEquals(expectedResponse, response);

        verify(projectRepository).getProjectById(1L);
        verify(projectMapper).toGetProjectResponse(project);
    }

    @Test
    void testGetProjectNotFound() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchProjectException.class, () -> projectService.getProject(1L));

        verify(projectRepository).getProjectById(1L);
        verify(projectMapper, never()).toGetProjectResponse(any());
    }

    @Test
    void testDeleteProject() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.of(project));

        // When
        projectService.deleteProject(1L);

        // Then
        verify(projectRepository).getProjectById(1L);
        verify(projectRepository).deleteProjectById(1L);
    }

    @Test
    void testDeleteProjectNotFound() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchProjectException.class, () -> projectService.deleteProject(1L));

        verify(projectRepository).getProjectById(1L);
        verify(projectRepository, never()).deleteProjectById(1L);
    }

    @Test
    void testGetAllProjects() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Project> projects = List.of(project);

        GetProjectResponse expectedResponse = new GetProjectResponse(1L, "Test Project", "Test Description",
                ProjectStatus.CREATED, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), null);

        when(projectRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(projects);
        when(projectMapper.toGetProjectResponse(project)).thenReturn(expectedResponse);

        // When
        List<GetProjectResponse> response = projectService.getAllProjects(pageable, null, null, null);

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(expectedResponse, response.get(0));

        verify(projectRepository).findAll(any(Specification.class), eq(pageable));
        verify(projectMapper).toGetProjectResponse(project);
    }

    @Test
    void testUpdateProject() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.of(project));
        when(projectMapper.toUpdateProjectResponse(project)).thenReturn(
                new UpdateProjectResponse("Updated Project", "Updated Description",
                        ProjectStatus.CREATED, LocalDate.of(2024, 2, 1), LocalDate.of(2024, 11, 30)));

        // When
        UpdateProjectResponse response = projectService.updateProject(updateProjectRequest, 1L);

        // Then
        assertNotNull(response);
        assertEquals("Updated Project", project.getName());
        assertEquals("Updated Description", project.getDescription());
        assertEquals(LocalDate.of(2024, 2, 1), project.getStartDate());
        assertEquals(LocalDate.of(2024, 11, 30), project.getEndDate());

        verify(projectRepository).getProjectById(1L);
        verify(projectMapper).toUpdateProjectResponse(project);
    }

    @Test
    void testUpdateProjectNotFound() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchProjectException.class,
                () -> projectService.updateProject(updateProjectRequest, 1L));

        verify(projectRepository).getProjectById(1L);
        verify(projectMapper, never()).toUpdateProjectResponse(any());
    }

    @Test
    void testUpdateStatus() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.of(project));

        // When
        UpdateStatusResponse response = projectService.updateStatus(updateStatusRequest, 1L);

        // Then
        assertNotNull(response);
        assertEquals(ProjectStatus.IN_PROGRESS, project.getStatus());
        assertEquals(ProjectStatus.IN_PROGRESS, response.projectStatus());

        verify(projectRepository).getProjectById(1L);
    }

    @Test
    void testUpdateStatusNotFound() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchProjectException.class,
                () -> projectService.updateStatus(updateStatusRequest, 1L));

        verify(projectRepository).getProjectById(1L);
    }

    @Test
    void testDeleteProjectTagsNotFound() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.of(project));
        when(projectTagRepository.existsByName("nonexistent")).thenReturn(false);

        // When & Then
        assertThrows(NoSuchTagException.class,
                () -> projectService.deleteProjectTags(1L, "nonexistent", false));

        verify(projectRepository).getProjectById(1L);
        verify(projectTagRepository).existsByName("nonexistent");
    }
}
