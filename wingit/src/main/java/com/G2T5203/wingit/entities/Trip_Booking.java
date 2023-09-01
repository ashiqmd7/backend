package com.G2T5203.wingit.entities;

public class Trip_Booking {
    private String BOOKING_ID;
    private double charged_price;

    public Trip_Booking(String BOOKING_ID, double charged_price) {
        this.BOOKING_ID = BOOKING_ID;
        this.charged_price = charged_price;
    }

    public String getBOOKING_ID() {
        return BOOKING_ID;
    }

    public void setBOOKING_ID(String BOOKING_ID) {
        this.BOOKING_ID = BOOKING_ID;
    }

    public double getCharged_price() {
        return charged_price;
    }

    public void setCharged_price(double charged_price) {
        this.charged_price = charged_price;
    }

    @Override
    public String toString() {
        return "Trip_Booking{" +
                "BOOKING_ID='" + BOOKING_ID + '\'' +
                ", charged_price=" + charged_price +
                '}';
    }
}
