package com.baticuisine.repository;

import com.baticuisine.models.Devis;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DevisRepository {
    Optional<Devis> findById(UUID id);
    List<Devis> findAll();
    Optional<Devis> save(Devis devis);
    Optional<Devis> updateStatus(UUID devisId, boolean isAccepted);
    boolean deleteById(UUID id);
}
