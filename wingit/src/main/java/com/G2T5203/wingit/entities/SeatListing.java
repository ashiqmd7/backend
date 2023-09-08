package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

@Entity
@IdClass(SeatListingPk.class)
public class SeatListing {
    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "planeId", referencedColumnName = "planeId"),
            @JoinColumn(name = "routeId", referencedColumnName = "routeId"),
            @JoinColumn(name = "departureDatetime", referencedColumnName = "departureDatetime")
    })
    private RouteListing routeListing;
    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "planeId", referencedColumnName = "planeId"),
            @JoinColumn(name = "seatNumber", referencedColumnName = "seatNumber"),
    })
    private Seat seat;
    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;
    private String occupantName;

    public SeatListing(RouteListing routeListing, Seat seat, Booking booking, String occupantName) {
        this.routeListing = routeListing;
        this.seat = seat;
        this.booking = booking;
        this.occupantName = occupantName;
    }

    public SeatListing() {

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

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }

    @Override
    public String toString() {
        return "SeatListing{" +
                "routeListing=" + routeListing +
                ", seat=" + seat +
                ", booking=" + booking +
                ", occupantName='" + occupantName + '\'' +
                '}';
    }
}
