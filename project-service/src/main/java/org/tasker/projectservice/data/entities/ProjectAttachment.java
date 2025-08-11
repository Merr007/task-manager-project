package org.tasker.projectservice.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "project_attachments")
@Data
public class ProjectAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private Long filesize;

    private String contentType;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime uploadDate;

    private String filePath;
}
