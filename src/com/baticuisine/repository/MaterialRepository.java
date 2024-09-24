package com.baticuisine.repository;

import com.baticuisine.models.Material;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository {
    Optional<Material> add(Material material);

    Optional<Material> findByName(String name);

    Optional<Material> findById(UUID id);

    List<Material> getAllMaterials();

    boolean deleteById(UUID id);
}