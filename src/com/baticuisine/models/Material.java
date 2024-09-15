package com.baticuisine.models;

public class Material extends Component {

    private double unitCost;
    private int quantity;
    private double transportCost;

    public Material(String name, String componentType, double tvaRate, double unitCost, int quantity, double transportCost) {
        super(name, componentType, tvaRate); // Call the constructor of the abstract Component class
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.transportCost = transportCost;
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
}

