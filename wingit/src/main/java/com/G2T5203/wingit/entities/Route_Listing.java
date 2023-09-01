package com.G2T5203.wingit.entities;

import java.util.Date;

public class Route_Listing {
    private String PLANE_ID;
    private String ROUTE_ID;
    private Date departure_datetime;
    private double base_price;

    public Route_Listing(String PLANE_ID, String ROUTE_ID, Date departure_datetime, double base_price) {
        this.PLANE_ID = PLANE_ID;
        this.ROUTE_ID = ROUTE_ID;
        this.departure_datetime = departure_datetime;
        this.base_price = base_price;
    }

    public String getPLANE_ID() {
        return PLANE_ID;
    }

    public void setPLANE_ID(String PLANE_ID) {
        this.PLANE_ID = PLANE_ID;
    }

    public String getROUTE_ID() {
        return ROUTE_ID;
    }

    public void setROUTE_ID(String ROUTE_ID) {
        this.ROUTE_ID = ROUTE_ID;
    }

    public Date getDeparture_datetime() {
        return departure_datetime;
    }

    public void setDeparture_datetime(Date departure_datetime) {
        this.departure_datetime = departure_datetime;
    }

    public double getBase_price() {
        return base_price;
    }

    public void setBase_price(double base_price) {
        this.base_price = base_price;
    }

    @Override
    public String toString() {
        return "Route_Listing{" +
                "PLANE_ID='" + PLANE_ID + '\'' +
                ", ROUTE_ID='" + ROUTE_ID + '\'' +
                ", departure_datetime=" + departure_datetime +
                ", base_price=" + base_price +
                '}';
    }
}
