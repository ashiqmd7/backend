package com.G2T5203.wingit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.Date;

@Entity
@IdClass(RouteListingID.class)
public class RouteListing {
    @Id
    private String planeId;
    @Id
    private String routeId;
    @Id
    private Date departureDatetime;
    private double basePrice;

    public RouteListing(String planeId, String routeId, Date departureDatetime, double basePrice) {
        this.planeId = planeId;
        this.routeId = routeId;
        this.departureDatetime = departureDatetime;
        this.basePrice = basePrice;
    }

    public RouteListing() {

    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeID) {
        this.planeId = planeID;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeID) {
        this.routeId = routeID;
    }

    public Date getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(Date departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "RouteListing{" +
                "planeID='" + planeId + '\'' +
                ", routeID='" + routeId + '\'' +
                ", departureDatetime=" + departureDatetime +
                ", basePrice=" + basePrice +
                '}';
    }
}
