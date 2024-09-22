package com.baticuisine.models;

import com.baticuisine.models.enumerations.ProjectState;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {

    private UUID id;
    private String projectName;
    private double profitMargin; // As a percentage (e.g., 0.2 for 20%)
    private double totalCost;
    private double area;
    private UUID clientId;
    private ProjectState projectState;
    private List<Component> components; // Store both materials and workforce

    // Constructor for new projects where totalCost will be calculated
    public Project(String projectName, double profitMargin, double area, ProjectState projectState) {
        this.id = UUID.randomUUID(); // Generate a UUID for the project
        this.projectName = projectName;
        this.profitMargin = profitMargin;
        this.area = area; // Initialize area
        this.projectState = projectState;
        this.components = new ArrayList<>();
    }

    // Constructor for projects retrieved from the database with totalCost
    public Project(UUID id, String projectName, double profitMargin, double totalCost, double area, ProjectState projectState, UUID clientId) {
        this.id = id;
        this.projectName = projectName;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.area = area; // Initialize area
        this.projectState = projectState;
        this.clientId = clientId;
        this.components = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder projectDetails = new StringBuilder();
        projectDetails.append("Project Name: ").append(projectName).append("\n")
                .append("Area: ").append(area).append(" sq.m.\n")
                .append("Profit Margin: ").append(profitMargin * 100).append("%\n")
                .append("Total Cost: ").append(totalCost).append("\n")
                .append("State: ").append(projectState).append("\n")
                .append("Client ID: ").append(clientId).append("\n")
                .append("Components: ").append(components.size()).append("\n");

        if (components != null && !components.isEmpty()) {
            // Loop through the components and append their details
            for (Component component : components) {
                projectDetails.append(component.toString()).append("\n"); // Call component's toString method
            }
        } else {
            projectDetails.append("No components available for this project.");
        }

        return projectDetails.toString();
    }


    // Method to calculate the total project cost
    public double calculateTotalCost() {
        double componentCost = components.stream()
                .mapToDouble(Component::calculateCost)
                .sum();

        // Apply the profit margin
        double finalCost = componentCost + (componentCost * profitMargin);

        // Set the total cost of the project
        this.totalCost = finalCost;

        return finalCost;
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

    public void setArea(double area) { // New setter for area
        this.area = area;
    }

    public ProjectState getProjectState() {
        return projectState;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setClientId(UUID id) {
        this.clientId = id;
    }

    public UUID getClientId() {
        return clientId;
    }
}
