package com.baticuisine.config;

import com.baticuisine.impl.ClientRepositoryImpl;
import com.baticuisine.impl.ComponentRepositoryImpl;
import com.baticuisine.impl.ProjectRepositoryImpl;
import com.baticuisine.repository.ClientRepository;
import com.baticuisine.repository.ComponentRepository;
import com.baticuisine.repository.ProjectRepository;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.ProjectService;

public class ApplicationFactory {
    private final ClientRepository clientRepo;
    private final ProjectRepository projectRepo;
    private final ProjectService projectService;
    private final ClientService clientService;
    private final ComponentRepository componentRepo;

    public ApplicationFactory() {
        this.clientRepo = new ClientRepositoryImpl();
        this.projectRepo = new ProjectRepositoryImpl();
        this.componentRepo = new ComponentRepositoryImpl();
        this.clientService = new ClientService(clientRepo);
        this.projectService = new ProjectService(projectRepo, componentRepo, clientService);
    }

    public ProjectService getProjectService() {
        return projectService;
    }
    public ClientService getClientService() {
        return clientService;
    }

    public ClientRepository getClientRepository() {
        return clientRepo;
    }

    public ProjectRepository getProjectRepository() {
        return projectRepo;
    }

    public ComponentRepository getComponentRepository() {return componentRepo; }
}
