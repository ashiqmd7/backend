package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.entities.SeatListing;
import jakarta.persistence.JoinColumn;

import java.util.Date;

public class SeatListingSimpleJson {
    // RouteListingPk
    private String routePlaneId;
    public int routeId;
    public Date departureDatetime;

    // SeatPk
    public String seatPlaneId;
    public String seatNumber;

    // Booking's id
    public String bookingId;

    // the rest
    public String occupantName;

    public SeatListingSimpleJson(String routePlaneId, int routeId, Date departureDatetime, String seatPlaneId, String seatNumber, String bookingId, String occupantName) {
        this.routePlaneId = routePlaneId;
        this.routeId = routeId;
        this.departureDatetime = departureDatetime;
        this.seatPlaneId = seatPlaneId;
        this.seatNumber = seatNumber;
        this.bookingId = bookingId;
        this.occupantName = occupantName;
    }

    public SeatListingSimpleJson(SeatListing seatListing) {
        this(
                seatListing.getSeatListingPk().getRouteListing().getRouteListingPk().getPlane().getPlaneId(),
                seatListing.getSeatListingPk().getRouteListing().getRouteListingPk().getRoute().getRouteId(),
                seatListing.getSeatListingPk().getRouteListing().getRouteListingPk().getDepartureDatetime(),
                seatListing.getSeatListingPk().getSeat().getSeatPk().getPlane().getPlaneId(),
                seatListing.getSeatListingPk().getSeat().getSeatPk().getSeatNumber(),
                seatListing.getBooking().getBookingId(),
                seatListing.getOccupantName());
    }

    public String getRoutePlaneId() {
        return routePlaneId;
    }

    public void setRoutePlaneId(String routePlaneId) {
        this.routePlaneId = routePlaneId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public Date getDepartureDatetime() {
        return departureDatetime;
    }

    public void setDepartureDatetime(Date departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    public String getSeatPlaneId() {
        return seatPlaneId;
    }

    public void setSeatPlaneId(String seatPlaneId) {
        this.seatPlaneId = seatPlaneId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getOccupantName() {
        return occupantName;
    }

    public void setOccupantName(String occupantName) {
        this.occupantName = occupantName;
    }
}
