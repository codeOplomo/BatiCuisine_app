package com.baticuisine.models;

public abstract class Component {

    private String name;
    private String componentType;
    private double tvaRate;

    public Component(String name, String componentType, double tvaRate) {
        this.name = name;
        this.componentType = componentType;
        this.tvaRate = tvaRate;
    }

    // Getters and Setters
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

