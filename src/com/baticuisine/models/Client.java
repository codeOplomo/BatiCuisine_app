package com.baticuisine.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Client {

    private UUID id;
    private String name;
    private String phone;
    private String address;
    private boolean isPro;
    private List<Project> projects;

    public Client(String name, String phone, String address, boolean isPro) {
        this.id = UUID.randomUUID(); // Generate a UUID for the client
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.isPro = isPro;
        this.projects = new ArrayList<>();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public boolean isPro() {
        return isPro;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void addProject(Project project) {
        this.projects.add(project);
    }
}
