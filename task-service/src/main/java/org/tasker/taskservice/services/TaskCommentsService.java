package org.tasker.taskservice.services;

import org.springframework.data.domain.Pageable;
import org.tasker.taskservice.api.dto.create.CreateTaskCommentRequest;
import org.tasker.taskservice.api.dto.get.GetTaskCommentResponse;

import java.util.List;

public interface TaskCommentsService {

    void createComment(Long taskId, CreateTaskCommentRequest request);

    List<GetTaskCommentResponse> getComments(Long taskId, Pageable pageable, String userId);

    void deleteComment(Long taskId, Long commentId, String userId);
}
