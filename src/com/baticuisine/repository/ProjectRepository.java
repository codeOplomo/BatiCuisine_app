package com.baticuisine.repository;

import com.baticuisine.models.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {
    // Return Optional<Project> to indicate success or failure of addition
    Optional<Project> addProject(Project project);

    Optional<Project> updateProject(Project project);


    // Return Optional instead of Project
    Optional<Project> findByName(String name);

    Optional<Project> findById(UUID id);

    // Return a list of all projects
    List<Project> getAllProjects();
}
