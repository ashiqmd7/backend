package com.G2T5203.wingit.entities;

import java.io.Serializable;

public class SeatId implements Serializable {
    private String planeID;
    private int seatNumber;

    public SeatId(String planeID, int seatNumber) {
        this.planeID = planeID;
        this.seatNumber = seatNumber;
    }

}
