package com.baticuisine.repository;

import com.baticuisine.models.Workforce;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkforceRepository {
    Optional<Workforce> add(Workforce workforce);

    Optional<Workforce> findByName(String name);

    Optional<Workforce> findById(UUID id);

    List<Workforce> getAllWorkforces();
}
