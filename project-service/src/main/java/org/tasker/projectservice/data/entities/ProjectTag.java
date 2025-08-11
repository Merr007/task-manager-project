package org.tasker.projectservice.data.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity(name = "project_tags")
@Data
public class ProjectTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Project> projects;
}
