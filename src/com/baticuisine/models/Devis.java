package com.baticuisine.models;

import java.time.LocalDate;
import java.util.UUID;

public class Devis {
    private UUID id;
    private double estimatedAmount;
    private LocalDate emissionDate;
    private LocalDate validityDate;
    private boolean isAccepted;
    private UUID projectId;

    public Devis(LocalDate emissionDate, LocalDate validityDate, UUID projectId) {
        this.id = UUID.randomUUID();
        this.emissionDate = emissionDate;
        this.validityDate = validityDate;
        this.isAccepted = false;
        this.projectId = projectId;
    }

    public Devis(UUID id, double estimatedAmount, LocalDate emissionDate, LocalDate validityDate, boolean isAccepted, UUID projectId) {
        this.id = id;
        this.estimatedAmount = estimatedAmount;
        this.emissionDate = emissionDate;
        this.validityDate = validityDate;
        this.isAccepted = isAccepted;
        this.projectId = projectId;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public LocalDate getEmissionDate() {
        return emissionDate;
    }

    public LocalDate getValidityDate() {
        return validityDate;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }
}
