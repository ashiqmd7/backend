package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.entities.SeatListing;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SeatListingNotFoundException extends RuntimeException {
    SeatListingNotFoundException(SeatListing seatListing) {
        super("Could not find seatlisting " + seatListing);
    }
}
