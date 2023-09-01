package com.G2T5203.wingit.entities;

public class Seat {
    private String PLANE_ID;
    private String SEAT_ID;
    private String Class;
    private double price_factor;

    public Seat(String PLANE_ID, String SEAT_ID, String aClass, double price_factor) {
        this.PLANE_ID = PLANE_ID;
        this.SEAT_ID = SEAT_ID;
        this.Class = aClass;
        this.price_factor = price_factor;
    }

    public String getPLANE_ID() {
        return PLANE_ID;
    }

    public void setPLANE_ID(String PLANE_ID) {
        this.PLANE_ID = PLANE_ID;
    }

    public String getSEAT_ID() {
        return SEAT_ID;
    }

    public void setSEAT_ID(String SEAT_ID) {
        this.SEAT_ID = SEAT_ID;
    }

    public String getAClass() {
        return Class;
    }

    public void setClass(String aClass) {
        Class = aClass;
    }

    public double getPrice_factor() {
        return price_factor;
    }

    public void setPrice_factor(double price_factor) {
        this.price_factor = price_factor;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "PLANE_ID='" + PLANE_ID + '\'' +
                ", SEAT_ID='" + SEAT_ID + '\'' +
                ", Class='" + Class + '\'' +
                ", price_factor=" + price_factor +
                '}';
    }
}
