package com.baticuisine.repository;

import com.baticuisine.models.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    Optional<Client> addClientProjectAndComponents(Client client, Project project, List<Material> materials, List<Workforce> workforces);
    /*
    Optional<Client> addClientAndProject(Client client, Project project);
*/
    Optional<Client> findByName(String name);

    Optional<Client> findById(UUID id);

    List<Client> getAllClients();
}
