package com.baticuisine.services;

import com.baticuisine.models.Client;
import com.baticuisine.models.Project;
import com.baticuisine.models.enumerations.ProjectState;
import com.baticuisine.repository.ClientRepository;
import com.baticuisine.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

public class ProjectService {

    private final ProjectRepository projectRepo;
    private final ClientRepository clientRepo;

    public ProjectService(ProjectRepository projectRepo, ClientRepository clientRepo) {
        this.projectRepo = projectRepo;
        this.clientRepo = clientRepo;
    }

    public Client findClientByName(String name) {
        Optional<Client> clientOpt = clientRepo.findByName(name); // Add this method in ClientRepository
        return clientOpt.orElse(null);
    }
    public Client addClient(String name, String phone, String address, boolean isPro, String projectName, double area) {
        Client client = new Client(name, phone, address, isPro);
        ProjectState projectState = ProjectState.ONGOING;
        double profitMargin = 0.2;

        // Create a new project associated with the client
        Project project = new Project(projectName, profitMargin, area, projectState);

        // Save both client and project together in the repository
        Optional<Client> savedClient = clientRepo.addClientAndProject(client, project);

        // Return the saved client or null if there was an issue
        return savedClient.orElse(null);
    }

    public Project addProjectForClient(Client client, String projectName, double area) {
        ProjectState projectState = ProjectState.ONGOING;
        double profitMargin = 0.2;

        // Create the project and associate it with the client
        Project project = new Project(projectName, profitMargin, area, projectState);
        project.setClientId(client.getId());

        // Save the project to the database
        projectRepo.addProject(project);

        // Optionally add the project to the client's list for in-memory tracking
        client.addProject(project);

        return project; // Return the project if needed
    }

    public List<Project> getAllProjects() {
        return projectRepo.getAllProjects();
    }
}
