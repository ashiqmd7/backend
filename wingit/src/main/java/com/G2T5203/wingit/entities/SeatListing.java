package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

@Entity
public class SeatListing {
    @EmbeddedId
    private SeatListingPk seatListingPk;
    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;
    private String occupantName;

    public SeatListing(SeatListingPk seatListingPk, Booking booking, String occupantName) {
        this.seatListingPk = seatListingPk;
        this.booking = booking;
        this.occupantName = occupantName;
    }

    public SeatListing() {
    }

    public SeatListingPk getSeatListingPk() {
        return seatListingPk;
    }

    public void setSeatListingPk(SeatListingPk seatListingPk) {
        this.seatListingPk = seatListingPk;
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
                "seatListingPk=" + seatListingPk +
                ", booking=" + booking +
                ", occupantName='" + occupantName + '\'' +
                '}';
    }
}
