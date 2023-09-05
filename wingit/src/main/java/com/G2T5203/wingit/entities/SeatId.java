package com.G2T5203.wingit.entities;

import java.io.Serializable;

public class SeatId implements Serializable {
    private String planeId;
    private int seatNumber;

    public SeatId(String planeId, int seatNumber) {
        this.planeId = planeId;
        this.seatNumber = seatNumber;
    }

}
