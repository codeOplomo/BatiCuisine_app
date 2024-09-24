package com.baticuisine.services;

import com.baticuisine.impl.DevisRepositoryImpl;
import com.baticuisine.models.Devis;
import com.baticuisine.models.Project;
import com.baticuisine.repository.ComponentRepository;
import com.baticuisine.repository.DevisRepository;
import com.baticuisine.repository.ProjectRepository;

import java.util.Optional;
import java.util.UUID;

public class DevisService {
    private final DevisRepository devisRepo;
    private final ProjectRepository projectRepo;
    private final ComponentRepository componentRepo;
    private final ClientService clientService;

    // Constructor to initialize the repositories and services
    public DevisService(ProjectRepository projectRepo, ComponentRepository componentRepo, ClientService clientService) {
        this.devisRepo = new DevisRepositoryImpl();
        this.projectRepo = projectRepo;
        this.componentRepo = componentRepo;
        this.clientService = clientService;
    }

    // saveDevis method without needing to pass repositories/services as parameters
    public Optional<Devis> saveDevis(Devis devis) {

        ProjectService projectService = new ProjectService(projectRepo, componentRepo, clientService);

        UUID projectId = devis.getProjectId();
        Optional<Project> projectOpt = projectService.findById(projectId);

        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            double totalCost = project.getTotalCost();

            devis.setEstimatedAmount(totalCost);
        } else {
            System.out.println("Project not found for ID: " + projectId);
            return Optional.empty();
        }

        return devisRepo.save(devis);
    }


    public Optional<Devis> updateDevisAcceptance(Devis devis) {
        return devisRepo.updateStatus(devis.getId(), devis.isAccepted());
    }
}
