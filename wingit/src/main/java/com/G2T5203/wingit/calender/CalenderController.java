package com.G2T5203.wingit.calender;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

@RestController
@RequestMapping(path = "/rest/api")
public class CalenderController {

    @GetMapping(path = "/generate-calendar")
    public ResponseEntity<Resource> generateCalenderFile() {

        String eventDateStr = "07 Nov 2023";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        ZonedDateTime eventDateTime = LocalDate.parse(eventDateStr, formatter)
                .atStartOfDay(ZoneId.systemDefault());

        // Convert ZonedDateTime to milliseconds
        Long startDateTimeInMillis = eventDateTime.toInstant().toEpochMilli();
        Long endDateTimeInMillis = eventDateTime.plusHours(1).toInstant().toEpochMilli(); // Assuming the event duration is 1 hour

        java.util.Calendar calendarStartTime = new GregorianCalendar();
        calendarStartTime.setTimeInMillis(startDateTimeInMillis);

        // Time zone info
        TimeZone tz = calendarStartTime.getTimeZone();
        ZoneId zid = tz.toZoneId();

        /* Generate unique identifier */
        UidGenerator ug = new RandomUidGenerator();
        Uid uid = ug.generateUid();

        /* Create the event */
        String eventSummary = "Demo Day";
        LocalDateTime start = LocalDateTime.ofInstant(calendarStartTime.toInstant(), zid);
        LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDateTimeInMillis), zid);
        VEvent event = new VEvent(start, end, eventSummary);
        event.add(uid);

        // Add email addresses as attendees
        Attendee attendee1 = new Attendee("aungri@stinkysnail.com");
        Attendee attendee2 = new Attendee("daddychoi@sugadaddies.com");
        event.add(attendee1);
        event.add(attendee2);

        // Create an Organizer
        Organizer organizer = new Organizer();
        organizer.setValue("MAILTO:WingIt@world.com");
        event.add(organizer);

        /* Create calendar */
        Calendar icsCalendar = new Calendar();
        icsCalendar.add(new ProdId("-//WingIt//iCal4j 1.0//EN"));

        // Set the location
        Location location = new Location("Private Jet");

        // Add the Location to the event
        event.add(location);

        /* Add event to calendar */
        icsCalendar.add(event);

        byte[] calendarByte = icsCalendar.toString().getBytes();
        Resource resource = new ByteArrayResource(calendarByte);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mycalendar.ics");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

}