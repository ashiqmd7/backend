package com.G2T5203.wingit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void setPlaneId(String planeID) {
        this.planeId = planeID;
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
                "planeID='" + planeId + '\'' +
                ", capacity=" + capacity +
                ", model='" + model + '\'' +
                '}';
    }
}
