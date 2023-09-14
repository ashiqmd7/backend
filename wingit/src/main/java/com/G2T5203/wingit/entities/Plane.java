package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

@Entity
public class Plane {
    @Id
    private String planeId;
    private int capacity;
    private String model;

    public Plane(String planeId, int capacity, String model) {
        this.planeId = planeId;
        this.capacity = capacity;
        this.model = model;
    }

    public Plane() {

    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
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
                "planeId='" + planeId + '\'' +
                ", capacity=" + capacity +
                ", model='" + model + '\'' +
                '}';
    }
}
