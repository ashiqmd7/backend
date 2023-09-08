package com.G2T5203.wingit.entities;

import java.io.Serializable;
import java.util.Date;

public class RouteListingPk implements Serializable {
    private Plane plane;
    private Route route;
    private Date departureDatetime;

    public RouteListingPk(Plane plane, Route route, Date departureDatetime) {
        this.plane = plane;
        this.route = route;
        this.departureDatetime = departureDatetime;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Date getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(Date departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
