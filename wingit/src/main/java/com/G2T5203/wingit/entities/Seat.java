package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

@Entity
@IdClass(SeatId.class)
public class Seat {
    @Id
    private String planeID;
    @Id
    private int seatNumber;
    private String seatClass;
    private double priceFactor;

    public Seat(String planeID, int seatNumber, String seatClass, double priceFactor) {
        this.planeID = planeID;
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.priceFactor = priceFactor;
    }

    public Seat() {

    }

    public String getPlaneID() {
        return planeID;
    }

    public void setPlaneID(String planeID) {
        this.planeID = planeID;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public double getPriceFactor() {
        return priceFactor;
    }

    public void setPriceFactor(double priceFactor) {
        this.priceFactor = priceFactor;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "planeID='" + planeID + '\'' +
                ", seatNumber=" + seatNumber +
                ", seatClass='" + seatClass + '\'' +
                ", priceFactor=" + priceFactor +
                '}';
    }
}
