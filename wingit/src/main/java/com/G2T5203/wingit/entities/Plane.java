package com.G2T5203.wingit.entities;

public class Plane {
    private String PLANE_ID;
    private int capacity;
    private String model;

    public Plane(String PLANE_ID, int capacity, String model) {
        this.PLANE_ID = PLANE_ID;
        this.capacity = capacity;
        this.model = model;
    }

    public String getPLANE_ID() {
        return PLANE_ID;
    }

    public void setPLANE_ID(String PLANE_ID) {
        this.PLANE_ID = PLANE_ID;
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
                "PLANE_ID='" + PLANE_ID + '\'' +
                ", capacity=" + capacity +
                ", model='" + model + '\'' +
                '}';
    }
}
