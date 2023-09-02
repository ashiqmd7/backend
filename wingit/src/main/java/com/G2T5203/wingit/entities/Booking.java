package com.G2T5203.wingit.entities;

import java.util.Date;

public class Booking {
    private String HOLD_ID;
    private String USER_ID;
    private Date start_datetime;
    private int party_size;
    private RouteListing route_listing;

    public Booking(String HOLD_ID, String USER_ID, Date start_datetime, int party_size, RouteListing route_listing) {
        this.HOLD_ID = HOLD_ID;
        this.USER_ID = USER_ID;
        this.start_datetime = start_datetime;
        this.party_size = party_size;
        this.route_listing = route_listing;
    }

    public String getHOLD_ID() {
        return HOLD_ID;
    }

    public void setHOLD_ID(String HOLD_ID) {
        this.HOLD_ID = HOLD_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public Date getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(Date start_datetime) {
        this.start_datetime = start_datetime;
    }

    public int getParty_size() {
        return party_size;
    }

    public void setParty_size(int party_size) {
        this.party_size = party_size;
    }

    public RouteListing getRoute_listing() {
        return route_listing;
    }

    public void setRoute_listing(RouteListing route_listing) {
        this.route_listing = route_listing;
    }

    @Override
    public String toString() {
        return "Hold_Booking{" +
                "HOLD_ID='" + HOLD_ID + '\'' +
                ", USER_ID='" + USER_ID + '\'' +
                ", start_datetime=" + start_datetime +
                ", party_size=" + party_size +
                ", route_listing=" + route_listing +
                '}';
    }
}
