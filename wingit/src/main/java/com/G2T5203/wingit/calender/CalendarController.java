package com.G2T5203.wingit.calender;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.G2T5203.wingit.booking.Booking;
import com.G2T5203.wingit.booking.BookingController;
import com.G2T5203.wingit.booking.BookingService;
import com.G2T5203.wingit.user.UserBadRequestException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(path = "/rest/api")
public class CalendarController {
    private final BookingService bookingService;
    private final BookingController bookingController;
    private static final Logger logger = LoggerFactory.getLogger(CalendarController.class);


    public CalendarController(BookingService bookingService, BookingController bookingController) {
        this.bookingService = bookingService;
        this.bookingController = bookingController;
    }

    @GetMapping(path = "/generate-calendar/{bookingId}")
    public ResponseEntity<Resource> generateCalendarFile(@PathVariable int bookingId,
                                                         @AuthenticationPrincipal UserDetails userDetails,
                                                         @AuthenticationPrincipal Jwt jwt) {
        try {
            logger.info("Entering generateCalendarFile");

            String bookingUsername = bookingService.getBookingUserUsername(bookingId);
            logger.info("Booking username: {}", bookingUsername);

            Booking booking = bookingService.getBookingById(bookingId);

            bookingController.checkIfNotUserNorAdmin(bookingUsername, userDetails, jwt);

            if (booking == null || booking.getInboundRouteListing() == null) {
                logger.warn("Booking or inboundRouteListing is null.");
                return ResponseEntity.notFound().build();
            }

            String eventSummary = "Flight Booking";
            VEvent event = createICalEvent(booking, eventSummary);

            if (event == null) {
                logger.error("Failed to create iCalendar event.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ByteArrayResource("Failed to create iCalendar event".getBytes()));
            }

            // Convert the iCalendar event to a byte array
            Calendar icsCalendar = new Calendar();
            icsCalendar.getProperties().add(new ProdId("-//WingIt//iCal4j 1.0//EN"));
            icsCalendar.getComponents().add(event);
            byte[] calendarBytes = icsCalendar.toString().getBytes();

            // Create a Resource from the byte array
            Resource resource = new ByteArrayResource(calendarBytes);

            // Set response headers for the calendar file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mycalendar.ics");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            // Return the calendar file as a response
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            logger.error("Exception occurred: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ByteArrayResource(("Error: " + e.getMessage()).getBytes()));
        } finally {
            // Log method exit
            logger.info("Exiting generateCalendarFile");
        }
    }

    private VEvent createICalEvent(Booking booking, String eventSummary) {
        if (booking.getInboundRouteListing() != null) {
            LocalDateTime departureDatetime = booking.getOutboundRouteListing().getRouteListingPk().getDepartureDatetime();
            long flightDurationMinutes = booking.getInboundRouteListing().getRouteListingPk().getRoute().getFlightDuration().toMinutes();
            LocalDateTime arrivalDatetime = departureDatetime.plusMinutes(flightDurationMinutes);

            // Convert LocalDateTime to ZonedDateTime in the default system time zone
            ZoneId systemDefaultZone = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = arrivalDatetime.atZone(systemDefaultZone);

            // Convert ZonedDateTime to milliseconds
            Long startDateTimeInMillis = zonedDateTime.toInstant().toEpochMilli();
            Long endDateTimeInMillis = zonedDateTime.plusHours(1).toInstant().toEpochMilli(); // Assuming the event duration is 1 hour

            java.util.Calendar calendarStartTime = new GregorianCalendar();
            calendarStartTime.setTimeInMillis(startDateTimeInMillis);

            // Time zone info
            TimeZone tz = calendarStartTime.getTimeZone();
            ZoneId zid = tz.toZoneId();

            /* Generate unique identifier */
            UidGenerator ug = new RandomUidGenerator();
            Uid uid = ug.generateUid();

            LocalDateTime start = LocalDateTime.ofInstant(calendarStartTime.toInstant(), zid);
            LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDateTimeInMillis), zid);
            VEvent event = new VEvent(start, end, eventSummary);
            event.getProperties().add(uid);

            return event;
        } else {
            // Handle the case when there is no inboundRouteListing
            return null;
        }
    }
}
