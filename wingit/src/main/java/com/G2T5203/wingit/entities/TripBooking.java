package com.G2T5203.wingit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TripBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String bookingID;
    private double chargedPrice;

    public TripBooking(String bookingID, double chargedPrice) {
        this.bookingID = bookingID;
        this.chargedPrice = chargedPrice;
    }

    public TripBooking() {

    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public double getChargedPrice() {
        return chargedPrice;
    }

    public void setChargedPrice(double chargedPrice) {
        this.chargedPrice = chargedPrice;
    }

    @Override
    public String toString() {
        return "TripBooking{" +
                "bookingID='" + bookingID + '\'' +
                ", chargedPrice=" + chargedPrice +
                '}';
    }
}
