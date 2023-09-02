package com.G2T5203.wingit.entities;

import java.io.Serializable;
import java.util.Date;

public class RouteListingID implements Serializable {
    private String planeID;
    private String routeID;
    private Date departureDatetime;

    public RouteListingID(String planeID, String routeID, Date departureDatetime) {
        this.planeID = planeID;
        this.routeID = routeID;
        this.departureDatetime = departureDatetime;
    }
}
