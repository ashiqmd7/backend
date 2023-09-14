package com.G2T5203.wingit.entities;

import java.io.Serializable;

public class SeatPk implements Serializable {
    private Plane plane;
    private String seatNumber;

    public SeatPk(Plane plane, String seatNumber) {
        this.plane = plane;
        this.seatNumber = seatNumber;
    }

    public SeatPk() {
    }
}
