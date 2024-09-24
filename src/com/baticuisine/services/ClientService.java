package com.baticuisine.services;

import com.baticuisine.models.Client;
import com.baticuisine.models.Material;
import com.baticuisine.models.Project;
import com.baticuisine.models.Workforce;
import com.baticuisine.models.enumerations.ProjectState;
import com.baticuisine.repository.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClientService {
    private final ClientRepository clientRepo;

    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public Client addClient(String name, String phone, String address, boolean isPro, String projectName, double area, List<Material> materials, List<Workforce> workforces) {
        Client client = new Client(name, phone, address, isPro);
        UUID clientId = client.getId();
        ProjectState projectState = ProjectState.ONGOING;

        Project project = new Project(projectName, area, projectState, clientId);

        Optional<Client> savedClient = clientRepo.addClientProjectAndComponents(client, project, materials, workforces);

        return savedClient.orElse(null);
    }

    public Client findClientByName(String name) {
        Optional<Client> clientOpt = clientRepo.findByName(name);
        return clientOpt.orElse(null);
    }

    public Client findClientById(UUID clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId);
        return clientOpt.orElse(null);
    }

}
