package com.baticuisine.models;

import com.baticuisine.models.enumerations.ProjectState;
import com.baticuisine.services.ClientService;
import com.baticuisine.services.ProjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {

    private UUID id;
    private String projectName;
    private double profitMargin;
    private double totalCost;
    private double area;
    private UUID clientId;
    private ProjectState projectState;
    private boolean isCostCalculated;
    private List<Component> components;

    private ProjectService projectService;

    public Project(String projectName, double area, ProjectState projectState, UUID clientId) {
        this.id = UUID.randomUUID();
        this.projectName = projectName;
        this.profitMargin = 0.0;
        this.totalCost = 0.0;
        this.area = area;
        this.projectState = projectState;
        this.isCostCalculated = false;
        this.clientId = clientId;
        this.components = new ArrayList<>();
    }


    public Project(UUID id, String projectName, double profitMargin, double totalCost, double area, ProjectState projectState, boolean isCostCalculated, UUID clientId) {
        this.id = id;
        this.projectName = projectName;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.area = area;
        this.projectState = projectState;
        this.isCostCalculated = isCostCalculated;
        this.clientId = clientId;
        this.components = new ArrayList<>();
    }


    @Override
    public String toString() {
        StringBuilder projectDetails = new StringBuilder();
        String format = "%-20s: %s%n";

        projectDetails.append(String.format(format, "Project Name", projectName))
                //.append(String.format(format, "Client Name", clientName))
                .append(String.format(format, "Area", area + " sq.m."))
                .append(String.format(format, "Profit Margin", (profitMargin * 100) + "%"))
                .append(String.format(format, "Total Cost", totalCost))
                .append(String.format(format, "State", projectState))
                .append(String.format(format, "Client ID", clientId))
                .append(String.format(format, "Components", components.size()));

        if (components != null && !components.isEmpty()) {
            for (Component component : components) {
                projectDetails.append(component.toString()).append("\n");
            }
        } else {
            projectDetails.append("No components available for this project.");
        }
        return projectDetails.toString();
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

    public double getArea() { // New getter for area
        return area;
    }
    public boolean isCostCalculated() {
        return isCostCalculated;
    }

    public void setArea(double area) { // New setter for area
        this.area = area;
    }

    public ProjectState getProjectState() {
        return projectState;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setProfitMargin(double profitMargin) {
        this.profitMargin = profitMargin;
    }

    public void setCostCalculated(boolean isCostCalculated) {
        this.isCostCalculated = isCostCalculated;
    }

    public UUID getClientId() {
        return clientId;
    }
}
