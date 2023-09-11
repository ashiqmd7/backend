package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Plane {
    @Id
    private String planeId;
    private int capacity;
    private String model;
    @OneToMany(mappedBy = "plane", cascade = CascadeType.ALL)
    private List<Seat> seats;
    @OneToMany(mappedBy = "plane", cascade = CascadeType.ALL)
    private List<RouteListing> routeListings;

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

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<RouteListing> getRouteListings() {
        return routeListings;
    }

    public void setRouteListings(List<RouteListing> routeListings) {
        this.routeListings = routeListings;
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
