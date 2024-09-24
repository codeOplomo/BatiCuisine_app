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

    public Component(UUID id, String name, String componentType, double tvaRate) {
        this.id = id; // Use the UUID passed from the Material constructor
        this.name = name;
        this.componentType = componentType;
        this.tvaRate = tvaRate;
    }


    public abstract double calculateCost();

    @Override
    public String toString() {
        return String.format("---------- TYPE: %-10s | Name: %s", componentType, name);
    }



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

    public void setTvaRate(double tvaRate) {
        this.tvaRate = tvaRate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
