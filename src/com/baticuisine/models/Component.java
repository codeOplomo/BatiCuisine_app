package com.baticuisine.models;

import java.util.UUID;

public abstract class Component {

    private UUID id; // Unique identifier
    private String name;
    private String componentType;
    private double tvaRate;

    public Component(String name, String componentType, double tvaRate) {
        this.id = UUID.randomUUID(); // Generate a unique ID for each component
        this.name = name;
        this.componentType = componentType;
        this.tvaRate = tvaRate;
    }

    public abstract double calculateCost();

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getComponentType() {
        return componentType;
    }

    public double getTvaRate() {
        return tvaRate;
    }
}
