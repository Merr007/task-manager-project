package org.tasker.taskservice.services;

import org.springframework.data.domain.Pageable;
import org.tasker.taskservice.api.dto.create.CreateTaskCommentRequest;
import org.tasker.taskservice.api.dto.get.GetTaskCommentResponse;

import java.util.List;

public interface TaskCommentsService {

    void createComment(Long projectId, Long taskId, CreateTaskCommentRequest request);

    List<GetTaskCommentResponse> getComments(Long projectId, Long taskId, Pageable pageable);

    List<GetTaskCommentResponse> getCommentsByUserId(String userId);

    void deleteComment(Long projectId, Long taskId, Long commentId, String userId);
}
