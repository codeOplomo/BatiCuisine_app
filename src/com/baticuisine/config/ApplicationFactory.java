package com.baticuisine.config;

import com.baticuisine.impl.ClientRepositoryImpl;
import com.baticuisine.impl.ProjectRepositoryImpl;
import com.baticuisine.repository.ClientRepository;
import com.baticuisine.repository.ProjectRepository;
import com.baticuisine.services.ProjectService;

public class ApplicationFactory {
    private final ClientRepository clientRepo;
    private final ProjectRepository projectRepo;
    private final ProjectService projectService;

    public ApplicationFactory() {
        this.clientRepo = new ClientRepositoryImpl();
        this.projectRepo = new ProjectRepositoryImpl();
        this.projectService = new ProjectService(projectRepo, clientRepo);
    }

    public ProjectService getProjectService() {
        return projectService;
    }

    public ClientRepository getClientRepository() {
        return clientRepo;
    }

    public ProjectRepository getProjectRepository() {
        return projectRepo;
    }
}
