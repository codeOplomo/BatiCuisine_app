package com.baticuisine.services;

import com.baticuisine.models.*;
import com.baticuisine.models.enumerations.ProjectState;
import com.baticuisine.repository.ClientRepository;
import com.baticuisine.repository.ComponentRepository;
import com.baticuisine.repository.ProjectRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProjectService {

    private final ProjectRepository projectRepo;
    private final ComponentRepository componentRepo;
    private final ClientService clientService;

    public ProjectService(ProjectRepository projectRepo, ComponentRepository componentRepo, ClientService clientService) {
        this.projectRepo = projectRepo;
        this.componentRepo = componentRepo;
        this.clientService = clientService;
    }

    public Optional<Project> findByName(String projectName) {
        Optional<Project> projectOptional = projectRepo.findByName(projectName);

        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            List<Component> components = componentRepo.fetchComponentsForProject(project.getId());
            project.setComponents(components);
        }

        return projectOptional;
    }

    public Optional<Project> findById(UUID projectId) {
        Optional<Project> projectOptional = projectRepo.findById(projectId);

        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            List<Component> components = componentRepo.fetchComponentsForProject(project.getId());
            project.setComponents(components);
        }

        return projectOptional;
    }

    public List<Project> getAllProjects() {
        return projectRepo.getAllProjects();
    }

    public Project addProjectForClient(Client client, String projectName, double area) {
        ProjectState projectState = ProjectState.ONGOING;
        UUID clientId = client.getId();

        Project project = new Project(projectName, area, projectState, clientId);

        Optional<Project> savedProject = projectRepo.addProject(project);

        client.addProject(project);

        return savedProject.orElse(null);
    }

    public void updateProjectCostInfo(Project project) {
        Optional<Project> updatedProject = projectRepo.updateProject(project);

        if (updatedProject.isPresent()) {
            System.out.println("Project cost info updated successfully.");
        } else {
            System.out.println("Failed to update project cost info.");
        }
    }

    public String getClientNameById(UUID clientId) {
        Client client = clientService.findClientById(clientId);
        return (client != null) ? client.getName() : null;
    }
}

