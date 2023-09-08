package com.G2T5203.wingit.entities;

import java.io.Serializable;

public class SeatListingPk implements Serializable {
    private RouteListing routeListing;
    private Seat seat;

    public SeatListingPk(RouteListing routeListing, Seat seat) {
        this.routeListing = routeListing;
        this.seat = seat;
    }

    public RouteListing getRouteListing() {
        return routeListing;
    }

    public void setRouteListing(RouteListing routeListing) {
        this.routeListing = routeListing;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
