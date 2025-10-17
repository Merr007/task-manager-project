package org.tasker.taskservice.api.dto.get;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Task comment information response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetTaskCommentResponse(
        @Schema(description = "Comment ID")
        Long commentId,

        @Schema(description = "User ID")
        String userId,

        @Schema(description = "Comment content")
        String content,

        @Schema(description = "Creation date")
        LocalDateTime createdAt
) {
}
