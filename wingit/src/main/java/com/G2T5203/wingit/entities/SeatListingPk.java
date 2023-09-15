package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public class SeatListingPk implements Serializable {
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "routePlaneId", referencedColumnName = "planeId"),
            @JoinColumn(name = "routeId", referencedColumnName = "routeId"),
            @JoinColumn(name = "departureDatetime", referencedColumnName = "departureDatetime")
    })
    private RouteListing routeListing;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "seatPlaneId", referencedColumnName = "planeId"),
            @JoinColumn(name = "seatNumber", referencedColumnName = "seatNumber"),
    })
    private Seat seat;

    public SeatListingPk(RouteListing routeListing, Seat seat) {
        this.routeListing = routeListing;
        this.seat = seat;
    }

    public SeatListingPk() {}

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
