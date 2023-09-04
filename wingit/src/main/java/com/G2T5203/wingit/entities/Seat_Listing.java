package com.G2T5203.wingit.entities;

public class Seat_Listing {
    private RouteListing route_listing;
    private String SEAT_ID;
    private String occupant_name;
    private String USER_ID;
    private String BOOKING_ID;

    public Seat_Listing(RouteListing route_listing, String SEAT_ID, String occupant_name, String USER_ID, String BOOKING_ID) {
        this.route_listing = route_listing;
        this.SEAT_ID = SEAT_ID;
        this.occupant_name = occupant_name;
        this.USER_ID = USER_ID;
        this.BOOKING_ID = BOOKING_ID;
    }

    public RouteListing getRoute_listing() {
        return route_listing;
    }

    public void setRoute_listing(RouteListing route_listing) {
        this.route_listing = route_listing;
    }

    public String getSEAT_ID() {
        return SEAT_ID;
    }

    public void setSEAT_ID(String SEAT_ID) {
        this.SEAT_ID = SEAT_ID;
    }

    public String getOccupant_name() {
        return occupant_name;
    }

    public void setOccupant_name(String occupant_name) {
        this.occupant_name = occupant_name;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getBOOKING_ID() {
        return BOOKING_ID;
    }

    public void setBOOKING_ID(String BOOKING_ID) {
        this.BOOKING_ID = BOOKING_ID;
    }

    @Override
    public String toString() {
        return "Seat_Listing{" +
                "route_listing=" + route_listing +
                ", SEAT_ID='" + SEAT_ID + '\'' +
                ", occupant_name='" + occupant_name + '\'' +
                ", USER_ID='" + USER_ID + '\'' +
                ", BOOKING_ID='" + BOOKING_ID + '\'' +
                '}';
    }
}
