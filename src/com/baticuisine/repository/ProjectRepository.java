package com.baticuisine.repository;

import com.baticuisine.models.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {
    Optional<Project> addProject(Project project);

    Optional<Project> updateProject(Project project);

    Optional<Project> findByName(String name);

    Optional<Project> findById(UUID id);

    List<Project> getAllProjects();
}
