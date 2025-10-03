package org.tasker.taskservice.api.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Create new task comment request")
public record CreateTaskCommentRequest(
        @NotBlank(message = "User id must be not blank")
        @Schema(description = "User identifier")
        String userId,

        @NotBlank(message = "Comment must be not blank")
        @Schema(description = "Comment content")
        String content
) {
}
