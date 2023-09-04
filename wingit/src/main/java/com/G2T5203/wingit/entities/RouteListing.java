package com.G2T5203.wingit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.Date;

@Entity
@IdClass(RouteListingID.class)
public class RouteListing {
    @Id
    private String planeID;
    @Id
    private String routeID;
    @Id
    private Date departureDatetime;
    private double basePrice;

    public RouteListing(String planeID, String routeID, Date departureDatetime, double basePrice) {
        this.planeID = planeID;
        this.routeID = routeID;
        this.departureDatetime = departureDatetime;
        this.basePrice = basePrice;
    }

    public RouteListing() {

    }

    public String getPlaneID() {
        return planeID;
    }

    public void setPlaneID(String planeID) {
        this.planeID = planeID;
    }

    public String getRouteID() {
        return routeID;
    }

    public void setRouteID(String routeID) {
        this.routeID = routeID;
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
                "planeID='" + planeID + '\'' +
                ", routeID='" + routeID + '\'' +
                ", departureDatetime=" + departureDatetime +
                ", basePrice=" + basePrice +
                '}';
    }
}
