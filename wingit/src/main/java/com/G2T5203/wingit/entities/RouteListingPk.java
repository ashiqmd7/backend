package com.G2T5203.wingit.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Embeddable
public class RouteListingPk implements Serializable {
    @ManyToOne
    @JoinColumn(name = "planeId")
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "routeId")
    private Route route;
    private Date departureDatetime;

    public RouteListingPk(Plane plane, Route route, Date departureDatetime) {
        this.plane = plane;
        this.route = route;
        this.departureDatetime = departureDatetime;
    }

    public RouteListingPk() {}

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteListingPk that = (RouteListingPk) o;
        return Objects.equals(plane, that.plane) && Objects.equals(route, that.route) && Objects.equals(departureDatetime, that.departureDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plane, route, departureDatetime);
    }
}
