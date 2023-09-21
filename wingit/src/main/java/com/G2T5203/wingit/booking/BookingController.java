package com.G2T5203.wingit.booking;

import com.G2T5203.wingit.entities.Booking;
import jakarta.validation.Valid;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
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

    @GetMapping(path = "/bookings/{username}")
    public List<BookingSimpleJson> getUserBookings(@PathVariable String username) {
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

    @PutMapping("bookings/update/{bookingId}")
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

    @PutMapping("bookings/update/{bookingId}/{paymentStatus}")
    public Booking updateIsPaid(@PathVariable int bookingId, @Valid @RequestBody boolean paymentStatus) {
        try {
            return service.updateIsPaid(bookingId, paymentStatus);
        } catch (Exception e) {
            throw new BookingBadRequestException(e);
        }
    }
}
