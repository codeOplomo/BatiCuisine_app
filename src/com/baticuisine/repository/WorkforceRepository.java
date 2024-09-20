package com.baticuisine.repository;

import com.baticuisine.models.Workforce;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkforceRepository {
    // Return Optional<Workforce> to indicate success or failure of addition
    Optional<Workforce> add(Workforce workforce);

    // Return Optional instead of Workforce
    Optional<Workforce> findByName(String name);

    Optional<Workforce> findById(UUID id);

    // Return a list of all workforces
    List<Workforce> getAllWorkforces();
}
