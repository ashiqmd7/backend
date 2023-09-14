package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

@Entity
public class Seat {
    @EmbeddedId
    private SeatPk seatPk;
    private String seatClass;
    private double priceFactor;

    public Seat(SeatPk seatPk, String seatClass, double priceFactor) {
        this.seatPk = seatPk;
        this.seatClass = seatClass;
        this.priceFactor = priceFactor;
    }

    public Seat() {
    }

    public SeatPk getSeatPk() {
        return seatPk;
    }

    public void setSeatPk(SeatPk seatPk) {
        this.seatPk = seatPk;
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
                "seatPk=" + seatPk +
                ", seatClass='" + seatClass + '\'' +
                ", priceFactor=" + priceFactor +
                '}';
    }
}
