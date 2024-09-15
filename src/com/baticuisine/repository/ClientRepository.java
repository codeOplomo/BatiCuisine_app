package com.baticuisine.repository;

import com.baticuisine.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    // Return Optional<Client> to indicate success or failure of addition
    Optional<Client> addClient(Client client);

    // Return Optional instead of Client
    Optional<Client> findClientByName(String name);

    // Return a list of all clients
    List<Client> getAllClients();
}
