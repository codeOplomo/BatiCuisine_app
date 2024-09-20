package com.baticuisine.impl;

import com.baticuisine.models.Material;
import com.baticuisine.repository.MaterialRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MaterialRepositoryImpl implements MaterialRepository {
    private final List<Material> materialList = new ArrayList<>();

    @Override
    public Optional<Material> add(Material material) {
        if (materialList.stream().anyMatch(m -> m.getName().equals(material.getName()))) {
            return Optional.empty();
        }
        materialList.add(material);
        return Optional.of(material);
    }

    @Override
    public Optional<Material> findById(UUID id) {
        return materialList.stream().filter(material -> material.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Material> findByName(String name) {
        return materialList.stream().filter(material -> material.getName().equals(name)).findFirst();
    }

    @Override
    public List<Material> getAllMaterials() {
        return new ArrayList<>(materialList);
    }


    @Override
    public boolean deleteById(UUID id) {
        return materialList.removeIf(material -> material.getId().equals(id));
    }
}
