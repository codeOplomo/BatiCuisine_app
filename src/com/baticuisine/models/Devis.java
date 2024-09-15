package com.baticuisine.models;

import java.util.UUID;

public class Devis {

    private UUID id;
    private double estimatedAmount;
    private String emissionDate;
    private boolean isAccepted;

    public Devis(double estimatedAmount, String emissionDate, boolean isAccepted) {
        this.id = UUID.randomUUID(); // Generate a UUID for the devis
        this.estimatedAmount = estimatedAmount;
        this.emissionDate = emissionDate;
        this.isAccepted = isAccepted;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public String getEmissionDate() {
        return emissionDate;
    }

    public boolean isAccepted() {
        return isAccepted;
    }
}

