package com.baticuisine.models;

import java.util.UUID;

public class Material extends Component {

    private double unitCost;
    private int quantity;
    private double transportCost;
    private double qualityCoefficient;

    public Material(String name, String componentType, double tvaRate, double unitCost, int quantity, double transportCost, double qualityCoefficient) {
        super(name, componentType, tvaRate);
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.transportCost = transportCost;
        this.qualityCoefficient = qualityCoefficient;
    }

    public Material(UUID id, String name, String componentType, double tvaRate, double unitCost, int quantity, double transportCost, double qualityCoefficient) {
        super(id, name, componentType, tvaRate);
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.transportCost = transportCost;
        this.qualityCoefficient = qualityCoefficient;
    }


    @Override
    public double calculateCost() {
        double materialCost = (unitCost * quantity) + transportCost;
        materialCost += materialCost * getTvaRate(); // Add TVA
        return materialCost;
    }

    @Override
    public String toString() {
        return String.format("%s, Unit Cost: %.2f, Quantity: %d",
                super.toString(), unitCost, quantity);
    }


    // Getters and Setters
    public double getUnitCost() {
        return unitCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTransportCost() {
        return transportCost;
    }

    public double getQualityCoefficient() {
        return qualityCoefficient;
    }

    public void setQualityCoefficient(double qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }


}
