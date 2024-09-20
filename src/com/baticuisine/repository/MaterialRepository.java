package com.baticuisine.repository;

import com.baticuisine.models.Material;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository {
    // Return Optional<Material> to indicate success or failure of addition
    Optional<Material> add(Material material);

    // Return Optional instead of Material
    Optional<Material> findByName(String name);

    Optional<Material> findById(UUID id);

    // Return a list of all materials
    List<Material> getAllMaterials();

    boolean deleteById(UUID id);
}