package com.G2T5203.wingit.booking;

import com.G2T5203.wingit.entities.Booking;
import com.G2T5203.wingit.user.UserBadRequestException;
import jakarta.validation.Valid;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }


    // TODO: Extract this function into common utilities (this is same code copied from UserController.
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
            if (isNeitherUserNorAdmin(username, jwt)) throw new UserBadRequestException("Not the same user.");
        } else if (userDetails != null) {
            if (isNeitherUserNorAdmin(username, userDetails)) throw new UserBadRequestException("Not the same user.");
        } else {
            throw new UserBadRequestException("No AuthenticationPrincipal provided for check.");
        }
    }


    @GetMapping(path = "/bookings/{username}")
    public List<BookingSimpleJson> getUserBookings(@PathVariable String username,
                                                   @AuthenticationPrincipal UserDetails userDetails, @AuthenticationPrincipal Jwt jwt) {
        checkIfNotUserNorAdmin(username, userDetails, jwt);

        return service.getAllBookingsByUser(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "bookings/new")
    public Booking createBooking(@Valid @RequestBody BookingSimpleJson newBookingSimpleJson) {
        try {
            return service.createBooking(newBookingSimpleJson);
        } catch (Exception e) {
            throw new BookingBadRequestException(e);
        }
    }

    @PutMapping("bookings/updateInbound/{bookingId}")
    public Booking updateInboundBooking(@PathVariable int bookingId, @Valid @RequestBody Map<String, Object> inboundRouteListingPk) {
        JSONObject jsonObj = new JSONObject(inboundRouteListingPk);
        try {
            String inboundPlaneId = jsonObj.getString("inboundPlaneId");
            int inboundRouteId = jsonObj.getInt("inboundRouteId");
            // Parse the inboundDepartureDatetime as a String from JSON
            String inboundDepartureDatetimeStr = jsonObj.getString("inboundDepartureDatetime");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // can change the format if needed
            Date inboundDepartureDatetime = dateFormat.parse(inboundDepartureDatetimeStr);

            return service.updateInboundBooking(bookingId, inboundPlaneId, inboundRouteId, inboundDepartureDatetime);
        } catch (Exception e) {
            throw new BookingBadRequestException(e);
        }
    }

    @PutMapping("bookings/updatePaymentStatus/{bookingId}/{paymentStatus}")
    public Booking updateIsPaid(@PathVariable int bookingId, @Valid @RequestBody boolean paymentStatus) {
        try {
            return service.updateIsPaid(bookingId, paymentStatus);
        } catch (Exception e) {
            throw new BookingBadRequestException(e);
        }
    }

    @DeleteMapping("bookings/delete/{bookingId}")
    public void deleteBooking(@PathVariable int bookingId) {
        try {
            service.deleteBookingById(bookingId);
        } catch (BookingNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new BookingBadRequestException(e);
        }
    }
}
