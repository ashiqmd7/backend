package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

@Entity
@IdClass(SeatId.class)
public class Seat {
    @Id
    private String planeId;
    @Id
    private int seatNumber;
    private String seatClass;
    private double priceFactor;

    public Seat(String planeId, int seatNumber, String seatClass, double priceFactor) {
        this.planeId = planeId;
        this.seatNumber = seatNumber;
        this.seatClass = seatClass;
        this.priceFactor = priceFactor;
    }

    public Seat() {

    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeID) {
        this.planeId = planeID;
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
                "planeID='" + planeId + '\'' +
                ", seatNumber=" + seatNumber +
                ", seatClass='" + seatClass + '\'' +
                ", priceFactor=" + priceFactor +
                '}';
    }
}
