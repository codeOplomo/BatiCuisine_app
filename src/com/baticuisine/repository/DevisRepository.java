package com.baticuisine.repository;

import com.baticuisine.models.Devis;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DevisRepository {
    Optional<Devis> findById(UUID id);
    List<Devis> findAll();
    Devis save(Devis devis);
    boolean deleteById(UUID id);
}
