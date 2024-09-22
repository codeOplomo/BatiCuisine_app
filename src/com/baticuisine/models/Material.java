package com.baticuisine.models;

public class Material extends Component {

    private double unitCost;
    private int quantity;
    private double transportCost;
    private double qualityCoefficient;

    public Material(String name, String componentType, double tvaRate, double unitCost, int quantity, double transportCost, double qualityCoefficient) {
        super(name, componentType, tvaRate); // Call the constructor of the abstract Component class
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
        return super.toString() + ", Unit Cost: " + unitCost + ", Quantity: " + quantity;
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

}
