package com.G2T5203.wingit.entities;

import java.time.Duration;

public class Route {
    private String ROUTE_ID;
    private String departure_dest;
    private String arrival_dest;
    private Duration flight_duration;

    public Route(String ROUTE_ID, String departure_dest, String arrival_dest, Duration flight_duration) {
        this.ROUTE_ID = ROUTE_ID;
        this.departure_dest = departure_dest;
        this.arrival_dest = arrival_dest;
        this.flight_duration = flight_duration;
    }

    public String getROUTE_ID() {
        return ROUTE_ID;
    }

    public void setROUTE_ID(String ROUTE_ID) {
        this.ROUTE_ID = ROUTE_ID;
    }

    public String getDeparture_dest() {
        return departure_dest;
    }

    public void setDeparture_dest(String departure_dest) {
        this.departure_dest = departure_dest;
    }

    public String getArrival_dest() {
        return arrival_dest;
    }

    public void setArrival_dest(String arrival_dest) {
        this.arrival_dest = arrival_dest;
    }

    public Duration getFlight_duration() {
        return flight_duration;
    }

    public void setFlight_duration(Duration flight_duration) {
        this.flight_duration = flight_duration;
    }

    @Override
    public String toString() {
        return "Route{" +
                "ROUTE_ID='" + ROUTE_ID + '\'' +
                ", departure_dest='" + departure_dest + '\'' +
                ", arrival_dest='" + arrival_dest + '\'' +
                ", flight_duration=" + flight_duration +
                '}';
    }
}
