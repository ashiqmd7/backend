package com.G2T5203.wingit.entities;

import java.io.Serializable;

public class SeatPk implements Serializable {
    private Plane plane;
    private int seatNumber;

    public SeatPk(Plane plane, int seatNumber) {
        this.plane = plane;
        this.seatNumber = seatNumber;
    }

}
