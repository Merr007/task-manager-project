package org.tasker.projectservice.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Schema(description = "Create new project request", example = """
       {
          "projectName": "MyProject",
          "description": "This is a really great project!",
          "startDate": "2024-01-15",
          "endDate": "2024-12-20",
          "projectTags": [
            {
              "name": "backend"
            },
            {
              "name": "frontend"
            }
          ],
          "projectMembers": [
            {
              "userId": 123,
              "role": "OWNER"
            },
            {
              "userId": 456,
              "role": "ADMIN"
            }
          ]
       }""")
public record CreateProjectRequest(
        @NotBlank(message = "Project name must be not blank")
        @Schema(description = "Project name")
        String projectName,

        @Schema(description = "Project description")
        String description,

        @Schema(description = "Project start date", example = "2024-01-01")
        LocalDate startDate,

        @Schema(description = "Project end date", example = "2024-01-01")
        LocalDate endDate,

        @UniqueElements(message = "Tags should be unique")
        @Schema(description = "Project tags")
        Set<CreateProjectTagRequest> projectTags,

        @Schema(description = "Project members")
        List<CreateProjectMemberRequest> projectMembers
) {
}
