package com.G2T5203.wingit.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@IdClass(RouteListingPk.class)
public class RouteListing {
    @Id
    @ManyToOne
    @JoinColumn(name = "planeId")
    private Plane plane;
    @Id
    @ManyToOne
    @JoinColumn(name = "routeId")
    private Route route;
    @Id
    private Date departureDatetime;
    private double basePrice;
    @OneToMany(mappedBy = "outboundRouteListing", cascade = CascadeType.ALL)
    private List<Booking> outboundBooking;
    @OneToMany(mappedBy = "inboundRouteListing", cascade = CascadeType.ALL)
    private List<Booking> inboundBooking;
    @OneToMany(mappedBy = "routeListing", cascade = CascadeType.ALL)
    private List<SeatListing> seatListing;

    public RouteListing(Plane plane, Route route, Date departureDatetime, double basePrice) {
        this.plane = plane;
        this.route = route;
        this.departureDatetime = departureDatetime;
        this.basePrice = basePrice;
    }

    public RouteListing() {

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

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public List<Booking> getOutboundBooking() {
        return outboundBooking;
    }

    public void setOutboundBooking(List<Booking> outboundBooking) {
        this.outboundBooking = outboundBooking;
    }

    public List<Booking> getInboundBooking() {
        return inboundBooking;
    }

    public void setInboundBooking(List<Booking> inboundBooking) {
        this.inboundBooking = inboundBooking;
    }

    public List<SeatListing> getSeatListing() {
        return seatListing;
    }

    public void setSeatListing(List<SeatListing> seatListing) {
        this.seatListing = seatListing;
    }

    @Override
    public String toString() {
        return "RouteListing{" +
                "plane=" + plane +
                ", route=" + route +
                ", departureDatetime=" + departureDatetime +
                ", basePrice=" + basePrice +
                '}';
    }
}
