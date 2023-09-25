package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.booking.BookingBadRequestException;
import com.G2T5203.wingit.entities.SeatListing;
import com.G2T5203.wingit.utils.DateUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class SeatListingController {
    private final SeatListingService service;

    public SeatListingController(SeatListingService service) {
        this.service = service;
    }

    @GetMapping(path = "/seatListings")
    public List<SeatListingSimpleJson> getAllSeatListings() { return service.getAllSeatListings(); }

    @GetMapping(path = "/seatListings/matchingRouteListing")
    public List<SeatListingSimpleJson> getAllSeatListings(@RequestBody Map<String, Object> routeListingPk) {
        try {
            String planeId = (String) routeListingPk.get("planeId");
            int routeId = (Integer) routeListingPk.get("routeId");
            LocalDateTime departureDatetime = DateUtils.handledParseDateTime((String) routeListingPk.get("departureDatetime"));

            return service.getAllSeatListingsInRouteListing(planeId, routeId, departureDatetime);
        } catch (Exception e) {
            throw new BookingBadRequestException(e);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/seatListings/new")
    public SeatListingSimpleJson createSeatListing(@Valid @RequestBody SeatListingSimpleJson newSeatListingJson) {
        try {
            return new SeatListingSimpleJson(service.createSeatListing(newSeatListingJson));
        } catch (Exception e) {
            throw new SeatListingBadRequestException(e);
        }
    }

    @PutMapping(path = "/seatListings/bookSeat")
    public SeatListing bookSeatListing(@Valid @RequestBody SeatListingSimpleJson seatBookingInfo) {
        try {
            return service.setSeatListing(
                    seatBookingInfo.getPlaneId(),
                    seatBookingInfo.routeId,
                    seatBookingInfo.departureDatetime,
                    seatBookingInfo.seatNumber,
                    seatBookingInfo.bookingId,
                    seatBookingInfo.occupantName);
        } catch (Exception e) {
            throw new SeatListingBadRequestException(e);
        }
    }

    @PutMapping(path = "/seatListings/cancelSeatBooking")
    public SeatListing cancelSeatListingBooking(@Valid @RequestBody SeatListingSimpleJson seatBookingInfo) {
        try {
            return service.cancelSeatListingBooking(
                    seatBookingInfo.getPlaneId(),
                    seatBookingInfo.routeId,
                    seatBookingInfo.departureDatetime,
                    seatBookingInfo.seatNumber);
        } catch (Exception e) {
            throw new SeatListingBadRequestException(e);
        }
    }
}
