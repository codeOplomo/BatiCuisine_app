package com.baticuisine.repository;

import com.baticuisine.models.Component;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface ComponentRepository {
    public List<Component> fetchComponentsForProject(UUID projectId);
}
