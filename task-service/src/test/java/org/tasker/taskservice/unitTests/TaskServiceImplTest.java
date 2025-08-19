package org.tasker.taskservice.unitTests;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tasker.taskservice.api.dto.TaskCreateDto;
import org.tasker.taskservice.api.dto.TaskResponseDto;
import org.tasker.taskservice.api.dto.TaskUpdateDto;
import org.tasker.taskservice.data.entities.Task;
import org.tasker.taskservice.data.repositories.TaskRepository;
import org.tasker.taskservice.domain.TaskPriority;
import org.tasker.taskservice.events.TaskEventType;
import org.tasker.taskservice.exceptions.NoSuchTaskException;
import org.tasker.taskservice.mappers.TaskMapper;
import org.tasker.taskservice.service.TaskServiceImpl;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {


}
