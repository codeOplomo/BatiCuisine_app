package com.baticuisine.services;

import com.baticuisine.models.Client;
import com.baticuisine.repository.ClientRepository;

import java.util.Optional;
import java.util.UUID;

public class ClientService {
    private final ClientRepository clientRepo;

    public ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    public Client findClientById(UUID clientId) {
        Optional<Client> clientOpt = clientRepo.findById(clientId); // Add this method in ClientRepository
        return clientOpt.orElse(null);
    }

    public Client findClientByName(String name) {
        Optional<Client> clientOpt = clientRepo.findByName(name); // Add this method in ClientRepository
        return clientOpt.orElse(null);
    }
}
