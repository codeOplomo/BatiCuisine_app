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
    public Project(UUID id, String projectName, double profitMargin, double totalCost, double area, ProjectState projectState) {
        this.id = id;
        this.projectName = projectName;
        this.profitMargin = profitMargin;
        this.totalCost = totalCost;
        this.area = area; // Initialize area
        this.projectState = projectState;
        this.components = new ArrayList<>();
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

    // Add a component to the project (can be either Material or Workforce)
    public void addComponent(Component component) {
        this.components.add(component);
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

    public Object getClientId() {
        return clientId;
    }
}
