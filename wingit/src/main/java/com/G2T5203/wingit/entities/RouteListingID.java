package com.G2T5203.wingit.entities;

import java.io.Serializable;

public class RouteListingID implements Serializable {
    private String planeID;
    private String routeID;

    public RouteListingID(String planeID, String routeID) {
        this.planeID = planeID;
        this.routeID = routeID;
    }
}
