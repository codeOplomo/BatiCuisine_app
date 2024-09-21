package com.baticuisine.repository;

import com.baticuisine.models.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    // Return Optional<Client> to indicate success or failure of addition
    Optional<Client> addClientProjectAndComponents(Client client, Project project, List<Material> materials, List<Workforce> workforces);
    /*
    Optional<Client> addClientAndProject(Client client, Project project);
*/
    // Return Optional instead of Client
    Optional<Client> findByName(String name);

    Optional<Client> findById(UUID id);

    // Return a list of all clients
    List<Client> getAllClients();
}
