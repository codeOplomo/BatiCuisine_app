package com.baticuisine.repository;

import com.baticuisine.models.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    // Return Optional<Project> to indicate success or failure of addition
    Optional<Project> addProject(Project project);

    // Return Optional instead of Project
    Optional<Project> findProjectByName(String name);

    // Return a list of all projects
    List<Project> getAllProjects();
}
