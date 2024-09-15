package com.baticuisine.models;

public class Workforce extends Component {

    private double hourlyRate;
    private int workHours;
    private double workerProductivity;

    public Workforce(String name, String componentType, double tvaRate, double hourlyRate, int workHours, double workerProductivity) {
        super(name, componentType, tvaRate); // Call the constructor of the abstract Component class
        this.hourlyRate = hourlyRate;
        this.workHours = workHours;
        this.workerProductivity = workerProductivity;
    }

    // Getters and Setters
    public double getHourlyRate() {
        return hourlyRate;
    }

    public int getWorkHours() {
        return workHours;
    }

    public double getWorkerProductivity() {
        return workerProductivity;
    }
}

