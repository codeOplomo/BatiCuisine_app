package com.baticuisine.impl;

import com.baticuisine.models.Workforce;
import com.baticuisine.repository.WorkforceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkforceRepositoryImpl implements WorkforceRepository {
    private final List<Workforce> workforceList = new ArrayList<>();

    @Override
    public Optional<Workforce> add(Workforce workforce) {
        if (workforceList.stream().anyMatch(w -> w.getName().equals(workforce.getName()))) {
            return Optional.empty();
        }
        workforceList.add(workforce);
        return Optional.of(workforce);
    }

    @Override
    public Optional<Workforce> findByName(String name) {
        return workforceList.stream().filter(workforce -> workforce.getName().equals(name)).findFirst();
    }

    @Override
    public Optional<Workforce> findById(UUID id) {
        return workforceList.stream().filter(workforce -> workforce.getId().equals(id)).findFirst();
    }

    @Override
    public List<Workforce> getAllWorkforces() {
        return new ArrayList<>(workforceList);
    }
}
