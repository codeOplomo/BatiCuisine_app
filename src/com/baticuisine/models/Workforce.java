package com.baticuisine.models;

import java.util.UUID;

public class Workforce extends Component {

    private double hourlyRate;
    private int workHours;
    private double workerProductivity;

    public Workforce(String name, String componentType, double tvaRate, double hourlyRate, int workHours, double workerProductivity) {
        super(name, componentType, tvaRate);
        this.hourlyRate = hourlyRate;
        this.workHours = workHours;
        this.workerProductivity = workerProductivity;
    }

    public Workforce(UUID id, String name, String componentType, double tvaRate, double hourlyRate, int workHours, double workerProductivity) {
        super(id, name, componentType, tvaRate);
        this.hourlyRate = hourlyRate;
        this.workHours = workHours;
        this.workerProductivity = workerProductivity;
    }

    @Override
    public double calculateCost() {
        double workforceCost = (hourlyRate * workHours) * workerProductivity;
        workforceCost += workforceCost * getTvaRate();
        return workforceCost;
    }

    @Override
    public String toString() {
        return String.format("%s, Hourly Rate: %.2f, Work Hours: %d",
                super.toString(), hourlyRate, workHours);
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

    public void setWorkerProductivity(double workerProductivity) {
        this.workerProductivity = workerProductivity;
    }

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
