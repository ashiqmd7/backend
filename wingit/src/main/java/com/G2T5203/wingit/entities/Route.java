package com.G2T5203.wingit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Duration;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String routeId;
    private String departureDest;
    private String arrivalDest;
    private Duration flightDuration;

    public Route(String routeId, String departureDest, String arrivalDest, Duration flightDuration) {
        this.routeId = routeId;
        this.departureDest = departureDest;
        this.arrivalDest = arrivalDest;
        this.flightDuration = flightDuration;
    }

    public Route() {

    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeID) {
        this.routeId = routeID;
    }

    public String getDepartureDest() {
        return departureDest;
    }

    public void setDepartureDest(String departureDest) {
        this.departureDest = departureDest;
    }

    public String getArrivalDest() {
        return arrivalDest;
    }

    public void setArrivalDest(String arrivalDest) {
        this.arrivalDest = arrivalDest;
    }

    public Duration getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Duration flightDuration) {
        this.flightDuration = flightDuration;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeID='" + routeId + '\'' +
                ", departureDest='" + departureDest + '\'' +
                ", arrivalDest='" + arrivalDest + '\'' +
                ", flightDuration=" + flightDuration +
                '}';
    }
}
