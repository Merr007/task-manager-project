package org.tasker.taskservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tasker.taskservice.api.dto.create.CreateTaskCommentRequest;
import org.tasker.taskservice.api.dto.get.GetTaskCommentResponse;
import org.tasker.taskservice.data.entities.Task;
import org.tasker.taskservice.data.entities.TaskComment;
import org.tasker.taskservice.data.repositories.TaskCommentRepository;
import org.tasker.taskservice.data.repositories.TaskRepository;
import org.tasker.taskservice.data.specification.TaskCommentSpecification;
import org.tasker.taskservice.exception.NoSuchTaskException;
import org.tasker.taskservice.mappers.TaskMapper;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskCommentsServiceImpl implements TaskCommentsService {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final GrpcClientProjectService grpcClientProjectService;
    private final TaskMapper taskMapper;

    @Override
    @Transactional
    public void createComment(Long taskId, CreateTaskCommentRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskException("No such task with id " + taskId));

        List<String> projectMembers = grpcClientProjectService.getProjectMembers(task.getProjectId());

        if (!projectMembers.contains(request.userId())) {
            throw new RuntimeException("User is not a member of the project");
        }

        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setUserId(request.userId());
        comment.setContent(request.content());

        taskCommentRepository.save(comment);

        log.info("Created comment for task {} by user {}", taskId, request.userId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GetTaskCommentResponse> getComments(Long taskId, Pageable pageable, String userId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskException("No such task with id " + taskId));

        Specification<TaskComment> spec = TaskCommentSpecification.builder()
                .withTaskId(taskId)
                .withUserId(userId)
                .build();
        return taskCommentRepository.findAll(spec, pageable).stream()
                .map(taskMapper::toGetTaskCommentResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteComment(Long taskId, Long commentId, String userId) {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchTaskException("No such task with id " + taskId));

        TaskComment comment = taskCommentRepository.findByIdAndTaskIdAndUserId(commentId, taskId, userId)
                .orElseThrow(() -> new RuntimeException("Comment not found or access denied"));

        taskCommentRepository.delete(comment);

        log.info("Deleted comment {} for task {} by user {}", commentId, taskId, userId);
    }
}
