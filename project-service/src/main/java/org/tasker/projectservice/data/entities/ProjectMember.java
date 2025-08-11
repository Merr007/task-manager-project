package org.tasker.projectservice.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.tasker.projectservice.domain.model.ProjectRole;

import java.time.LocalDateTime;

@Entity(name = "project_members")
@Data
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @Column(columnDefinition = "DATE")
    private LocalDateTime joinedAt;
}
