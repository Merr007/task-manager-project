package org.tasker.taskservice.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.tasker.taskservice.domain.model.TaskPriority;
import org.tasker.taskservice.domain.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "tasks")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Column(columnDefinition = "TIMESTAMP")
    // Планируемая дата
    private LocalDateTime dueDate;

    @Column(columnDefinition = "TIMESTAMP")
    // Фактическая дата завершения
    private LocalDateTime completionDate;

    private String assigneeId;

    @Column(nullable = false)
    private Long projectId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TaskAttachment> attachments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TaskComment> comments = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Task that = (Task) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}


