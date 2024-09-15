package com.baticuisine.models;

import com.baticuisine.models.enumerations.ProjectState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {

    private UUID id;
    private String projectName;
    private double profitMargin;
    private double totalCost;
    private ProjectState projectState;
    private List<Devis> devisList;
    private List<Material> materials;

    public Project(String projectName, double profitMargin, double totalCost, ProjectState projectState) {
        this.id = UUID.randomUUID(); // Generate a UUID for the project
        this.projectName = projectName;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.projectState = projectState;
        this.devisList = new ArrayList<>();
        this.materials = new ArrayList<>();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public ProjectState getProjectState() {
        return projectState;
    }

    public List<Devis> getDevisList() {
        return devisList;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void addDevis(Devis devis) {
        this.devisList.add(devis);
    }

    public void addMaterial(Material material) {
        this.materials.add(material);
    }
}
