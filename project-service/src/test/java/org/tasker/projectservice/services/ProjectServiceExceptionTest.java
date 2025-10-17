package org.tasker.projectservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tasker.projectservice.api.dto.create.CreateProjectMemberRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectRequest;
import org.tasker.projectservice.api.dto.create.CreateProjectTagRequest;
import org.tasker.projectservice.api.dto.update.UpdateProjectRequest;
import org.tasker.projectservice.data.entities.Project;
import org.tasker.projectservice.data.repositories.ProjectRepository;
import org.tasker.projectservice.domain.model.ProjectRole;
import org.tasker.projectservice.domain.model.ProjectStatus;
import org.tasker.projectservice.mappers.ProjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тесты негативных сценариев и обработки исключений в ProjectService
 */
@ExtendWith(MockitoExtension.class)
class ProjectServiceExceptionTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project project;
    private CreateProjectRequest createProjectRequest;
    private UpdateProjectRequest updateProjectRequest;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStartDate(LocalDate.of(2024, 1, 1));
        project.setEndDate(LocalDate.of(2024, 12, 31));
        project.setStatus(ProjectStatus.CREATED);

        createProjectRequest = new CreateProjectRequest(
                "Test Project",
                "Test Description",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                Set.of(new CreateProjectTagRequest("backend")),
                List.of(new CreateProjectMemberRequest("user123", ProjectRole.ADMIN))
        );

        updateProjectRequest = new UpdateProjectRequest(
                "Updated Project",
                "Updated Description",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 11, 30)
        );

    }

    @Test
    void testGetProjectWithRepositoryException() {
        // Given
        when(projectRepository.getProjectById(1L)).thenThrow(new RuntimeException("Database connection error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> projectService.getProject(1L));

        verify(projectRepository).getProjectById(1L);
        verify(projectMapper, never()).toGetProjectResponse(any());
    }

    @Test
    void testDeleteProjectWithRepositoryException() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.of(project));
        doThrow(new RuntimeException("Delete failed")).when(projectRepository).deleteProjectById(1L);

        // When & Then
        assertThrows(RuntimeException.class, () -> projectService.deleteProject(1L));

        verify(projectRepository).getProjectById(1L);
        verify(projectRepository).deleteProjectById(1L);
    }

    @Test
    void testUpdateProjectWithRepositoryException() {
        // Given
        when(projectRepository.getProjectById(1L)).thenReturn(Optional.of(project));
        when(projectMapper.toUpdateProjectResponse(project)).thenThrow(new RuntimeException("Mapping error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> projectService.updateProject(updateProjectRequest, 1L));

        verify(projectRepository).getProjectById(1L);
        verify(projectMapper).toUpdateProjectResponse(project);
    }

    @Test
    void testGetAllProjectsWithRepositoryException() {
        // Given
        when(projectRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(org.springframework.data.domain.Pageable.class))).thenThrow(new RuntimeException("Query failed"));

        // When & Then
        assertThrows(RuntimeException.class,
                () -> projectService.getAllProjects(org.springframework.data.domain.PageRequest.of(0, 10), null, null, null));

        verify(projectRepository).findAll(any(org.springframework.data.jpa.domain.Specification.class), any(org.springframework.data.domain.Pageable.class));
    }

    @Test
    void testCreateProjectWithMapperException() {
        // Given
        when(projectMapper.toProject(createProjectRequest)).thenThrow(new RuntimeException("Mapping failed"));

        // When & Then
        assertThrows(RuntimeException.class, () -> projectService.createProject(createProjectRequest));

        verify(projectMapper).toProject(createProjectRequest);
        verify(projectRepository, never()).save(any());
    }

    @Test
    void testProjectWithNullFields() {
        // Given
        Project projectWithNulls = new Project();
        projectWithNulls.setId(1L);
        projectWithNulls.setName(null);
        projectWithNulls.setDescription(null);
        projectWithNulls.setStatus(null);

        when(projectRepository.getProjectById(1L)).thenReturn(Optional.of(projectWithNulls));
        when(projectMapper.toGetProjectResponse(projectWithNulls)).thenThrow(new NullPointerException("Null field access"));

        // When & Then
        assertThrows(NullPointerException.class, () -> projectService.getProject(1L));

        verify(projectRepository).getProjectById(1L);
        verify(projectMapper).toGetProjectResponse(projectWithNulls);
    }
}
