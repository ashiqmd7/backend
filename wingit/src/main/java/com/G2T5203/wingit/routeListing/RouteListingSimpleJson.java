package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.entities.RouteListing;

import java.util.Date;

public class RouteListingSimpleJson {
    private Integer routeId;
    private String planeId;
    private Date departureDatetime;
    private double basePrice;

    public RouteListingSimpleJson(Integer routeId, String planeId, Date departureDatetime, double basePrice) {
        this.routeId = routeId;
        this.planeId = planeId;
        this.departureDatetime = departureDatetime;
        this.basePrice = basePrice;
    }
    public RouteListingSimpleJson(RouteListing routeListing) {
        this(
                routeListing.getRouteListingPk().getRoute().getRouteId(),
                routeListing.getRouteListingPk().getPlane().getPlaneId(),
                routeListing.getRouteListingPk().getDepartureDatetime(),
                routeListing.getBasePrice()
        );
    }
    public RouteListingSimpleJson() {}

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public Date getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(Date departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }
}
