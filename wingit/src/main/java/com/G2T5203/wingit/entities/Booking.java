package com.G2T5203.wingit.entities;

import java.util.Date;

public class Booking {
    private String holdId;
    private String userId;
    private String planeId;
    private String routeId;
    private Date startBookingDatetime;
    private int partySize;
    private double chargedPrice;
    private boolean isPaid;

    public Booking(String holdId, String userId, String planeId, String routeId, Date startBookingDatetime, int partySize, double chargedPrice, boolean isPaid) {
        this.holdId = holdId;
        this.userId = userId;
        this.planeId = planeId;
        this.routeId = routeId;
        this.startBookingDatetime = startBookingDatetime;
        this.partySize = partySize;
        this.chargedPrice = chargedPrice;
        this.isPaid = isPaid;
    }

    public String getHoldId() {
        return holdId;
    }

    public void setHoldId(String holdId) {
        this.holdId = holdId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Date getStartBookingDatetime() {
        return startBookingDatetime;
    }

    public void setStartBookingDatetime(Date startBookingDatetime) {
        this.startBookingDatetime = startBookingDatetime;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public double getChargedPrice() {
        return chargedPrice;
    }

    public void setChargedPrice(double chargedPrice) {
        this.chargedPrice = chargedPrice;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "holdId='" + holdId + '\'' +
                ", userId='" + userId + '\'' +
                ", planeId='" + planeId + '\'' +
                ", routeId='" + routeId + '\'' +
                ", startBookingDatetime=" + startBookingDatetime +
                ", partySize=" + partySize +
                ", chargedPrice=" + chargedPrice +
                ", isPaid=" + isPaid +
                '}';
    }
}
