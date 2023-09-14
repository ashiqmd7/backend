package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.entities.SeatListing;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SeatListingController {
    private final SeatListingService service;

    public SeatListingController(SeatListingService service) {
        this.service = service;
    }

    @GetMapping(path = "/seatListings")
    public List<SeatListingSimpleJson> getAllSeatListings() {
        return service.getAllSeatListings();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(path = "/seatListings/new")
    public SeatListing createSeatListing(@Valid @RequestBody SeatListingSimpleJson newSeatListingJson) {
        try {
            return service.createSeatListing(newSeatListingJson);
        } catch (Exception e) {
            throw new SeatListingBadRequestException(e);
        }
    }
}
