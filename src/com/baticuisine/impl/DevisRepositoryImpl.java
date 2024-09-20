package com.baticuisine.impl;

import com.baticuisine.models.Devis;
import com.baticuisine.repository.DevisRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DevisRepositoryImpl implements DevisRepository {
    private final List<Devis> devisList = new ArrayList<>();

    @Override
    public Optional<Devis> findById(UUID id) {
        return devisList.stream().filter(devis -> devis.getId().equals(id)).findFirst();
    }

    @Override
    public List<Devis> findAll() {
        return new ArrayList<>(devisList);
    }

    @Override
    public Devis save(Devis devis) {
        devisList.removeIf(d -> d.getId().equals(devis.getId()));
        devisList.add(devis);
        return devis;
    }

    @Override
    public boolean deleteById(UUID id) {
        return devisList.removeIf(devis -> devis.getId().equals(id));
    }
}
