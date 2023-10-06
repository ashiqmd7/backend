package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.booking.BookingBadRequestException;
import com.G2T5203.wingit.entities.SeatListing;
import com.G2T5203.wingit.user.UserBadRequestException;
import com.G2T5203.wingit.utils.DateUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class SeatListingController {
    private boolean isAdmin(UserDetails userDetails) { return userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")); }
    private boolean isAdmin(Jwt jwt) { return jwt.getClaim("role").equals("ROLE_ADMIN"); }
    private boolean isNeitherUserNorAdmin(String username, UserDetails userDetails) {
        boolean isUser = username.equals(userDetails.getUsername());
        boolean isAdmin = isAdmin(userDetails);

        return (!isUser && !isAdmin);
    }
    private boolean isNeitherUserNorAdmin(String username, Jwt jwt) {
        boolean isUser = username.equals(jwt.getClaim("sub"));
        boolean isAdmin = isAdmin(jwt);

        return (!isUser && !isAdmin);
    }
    private void checkIfNotUserNorAdmin(String username, UserDetails userDetails, Jwt jwt) {
        if (jwt != null) {
            if (isNeitherUserNorAdmin(username, jwt)) throw new SeatListingBadRequestException("Not the same user.");
        } else if (userDetails != null) {
            if (isNeitherUserNorAdmin(username, userDetails)) throw new SeatListingBadRequestException("Not the same user.");
        } else {
            throw new SeatListingBadRequestException("No AuthenticationPrincipal provided for check.");
        }
    }

    private final SeatListingService service;

    public SeatListingController(SeatListingService service) {
        this.service = service;
    }

    @GetMapping(path = "/seatListings")
    public List<SeatListingSimpleJson> getAllSeatListings() { return service.getAllSeatListings(); }

    @GetMapping(path = "/seatListings/matchingRouteListing/{planeId}/{routeId}/{departureDatetimeStr}")
    public List<SeatListingSimpleJson> getAllSeatListings(@PathVariable String planeId, @PathVariable Integer routeId, @PathVariable String departureDatetimeStr) {
        try {
            LocalDateTime departureDatetime = DateUtils.handledParseDateTime(departureDatetimeStr);
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

    @PutMapping(path = "/seatListings/bookSeat/reserve")
    public SeatListingSimpleJson reserveSeatListing(@Valid @RequestBody SeatListingSimpleJson seatBookingInfo) {
        try {
            if (seatBookingInfo.occupantName != null) throw new SeatListingBadRequestException("Reservation does not need occupant name yet!");
            SeatListing updatedSeatListing = service.reserveSeatListing(
                    seatBookingInfo.getPlaneId(),
                    seatBookingInfo.routeId,
                    seatBookingInfo.departureDatetime,
                    seatBookingInfo.seatNumber,
                    seatBookingInfo.bookingId);
            return new SeatListingSimpleJson(updatedSeatListing);
        } catch (Exception e) {
            throw new SeatListingBadRequestException(e);
        }
    }

    @PutMapping(path = "/seatListings/bookSeat/setOccupant")
    public SeatListingSimpleJson setOccupantForSeatListing(@Valid @RequestBody SeatListingSimpleJson seatBookingInfo) {
        try {
            if (seatBookingInfo.occupantName == null) throw new SeatListingBadRequestException("Occupant Name is Empty!");
            SeatListing updatedSeatListing = service.setOccupantForSeatListing(
                    seatBookingInfo.getPlaneId(),
                    seatBookingInfo.routeId,
                    seatBookingInfo.departureDatetime,
                    seatBookingInfo.seatNumber,
                    seatBookingInfo.bookingId,
                    seatBookingInfo.occupantName);
            return new SeatListingSimpleJson(updatedSeatListing);
        } catch (Exception e) {
            throw new SeatListingBadRequestException(e);
        }
    }

    @PutMapping(path = "/seatListings/cancelSeatBooking/{username}")
    public SeatListingSimpleJson cancelSeatListingBooking(@PathVariable String username, @Valid @RequestBody SeatListingSimpleJson seatBookingInfo,
                                                          @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal Jwt jwt) {
        checkIfNotUserNorAdmin(username, userDetails, jwt);
        try {
            SeatListing updatedSeatListing =  service.cancelSeatListingBooking(
                    seatBookingInfo.getPlaneId(),
                    seatBookingInfo.routeId,
                    seatBookingInfo.departureDatetime,
                    seatBookingInfo.seatNumber);
            return new SeatListingSimpleJson(updatedSeatListing);
        } catch (Exception e) {
            throw new SeatListingBadRequestException(e);
        }
    }
}
