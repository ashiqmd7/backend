package com.G2T5203.wingit.entities;

public class Plane {
    private String PlaneID;
    private int capacity;
    private String model;

    public Plane(String PlaneID, int capacity, String model) {
        this.PlaneID = PlaneID;
        this.capacity = capacity;
        this.model = model;
    }

    public String getPlaneID() {
        return PlaneID;
    }

    public void setPlaneID(String PlaneID) {
        this.PlaneID = PlaneID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return "Plane{" +
                "PlaneID='" + PlaneID + '\'' +
                ", capacity=" + capacity +
                ", model='" + model + '\'' +
                '}';
    }
}
