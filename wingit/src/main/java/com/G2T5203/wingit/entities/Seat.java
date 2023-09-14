package com.G2T5203.wingit.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
//@IdClass(SeatPk.class)
public class Seat {
    @EmbeddedId
    private SeatPk seatPk;
    private String seatClass;
    private double priceFactor;
//    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<SeatListing> seatListing;

    public Seat(SeatPk seatPk, String seatClass, double priceFactor) {
        this.seatPk = seatPk;
        this.seatClass = seatClass;
        this.priceFactor = priceFactor;
    }

    public Seat() {
    }

    public SeatPk getSeatPk() {
        return seatPk;
    }

    public void setSeatPk(SeatPk seatPk) {
        this.seatPk = seatPk;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public double getPriceFactor() {
        return priceFactor;
    }

    public void setPriceFactor(double priceFactor) {
        this.priceFactor = priceFactor;
    }

//    public List<SeatListing> getSeatListing() {
//        return seatListing;
//    }
//
//    public void setSeatListing(List<SeatListing> seatListing) {
//        this.seatListing = seatListing;
//    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatPk=" + seatPk +
                ", seatClass='" + seatClass + '\'' +
                ", priceFactor=" + priceFactor +
                '}';
    }

    //    @Id
//    @ManyToOne
//    @JoinColumn(name = "planeId")
//    private Plane plane;
//    @Id
//    private String seatNumber;
//    private String seatClass;
//    private double priceFactor;
//    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<SeatListing> seatListing;
//
//    public Seat(Plane plane, String seatNumber, String seatClass, double priceFactor) {
//        this.plane = plane;
//        this.seatNumber = seatNumber;
//        this.seatClass = seatClass;
//        this.priceFactor = priceFactor;
//    }
//
//    public Seat() {
//
//    }
//
//    public Plane getPlane() {
//        return plane;
//    }
//
//    public void setPlane(Plane plane) {
//        this.plane = plane;
//    }
//
//    public String getSeatNumber() {
//        return seatNumber;
//    }
//
//    public void setSeatNumber(String seatNumber) {
//        this.seatNumber = seatNumber;
//    }
//
//    public String getSeatClass() {
//        return seatClass;
//    }
//
//    public void setSeatClass(String seatClass) {
//        this.seatClass = seatClass;
//    }
//
//    public double getPriceFactor() {
//        return priceFactor;
//    }
//
//    public void setPriceFactor(double priceFactor) {
//        this.priceFactor = priceFactor;
//    }
//
//    public List<SeatListing> getSeatListing() {
//        return seatListing;
//    }
//
//    public void setSeatListing(List<SeatListing> seatListing) {
//        this.seatListing = seatListing;
//    }
//
//    @Override
//    public String toString() {
//        return "Seat{" +
//                "plane=" + plane +
//                ", seatNumber=" + seatNumber +
//                ", seatClass='" + seatClass + '\'' +
//                ", priceFactor=" + priceFactor +
//                '}';
//    }
}
