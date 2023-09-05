package com.G2T5203.wingit.entities;

import java.io.Serializable;
import java.util.Date;

public class RouteListingID implements Serializable {
    private String planeId;
    private String routeId;
    private Date departureDatetime;

    public RouteListingID(String planeId, String routeId, Date departureDatetime) {
        this.planeId = planeId;
        this.routeId = routeId;
        this.departureDatetime = departureDatetime;
    }
}
