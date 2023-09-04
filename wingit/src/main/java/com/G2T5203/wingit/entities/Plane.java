package com.G2T5203.wingit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Plane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String planeID;
    private int capacity;
    private String model;

    public Plane(String planeID, int capacity, String model) {
        this.planeID = planeID;
        this.capacity = capacity;
        this.model = model;
    }

    public Plane() {
    }

    public String getPlaneID() {
        return planeID;
    }

    public void setPlaneID(String planeID) {
        this.planeID = planeID;
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
                "planeID='" + planeID + '\'' +
                ", capacity=" + capacity +
                ", model='" + model + '\'' +
                '}';
    }
}
