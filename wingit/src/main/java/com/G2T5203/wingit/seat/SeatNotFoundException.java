package com.G2T5203.wingit.seat;

import com.G2T5203.wingit.entities.RouteListingPk;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(RouteListingPk pk) {
        super("Could not find route " + pk);
    }
}
