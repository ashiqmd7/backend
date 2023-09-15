package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.entities.RouteListing;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RouteListingController {
    private final RouteListingService service;

    public RouteListingController(RouteListingService service) { this.service = service; }

    @GetMapping(path = "/routeListings")
    public List<RouteListingSimpleJson> getAllRouteListings() { return service.getAllRouteListings(); }

    @GetMapping(path = "/routeListings/depart/{departureDest}")
    public List<RouteListingSimpleJson> getAllRouteListingsByDepartureDest(@PathVariable String departureDest) {
        return service.getAllRouteListingsWithDepartureDest(departureDest);
    }

    @GetMapping(path = "/routeListings/departAndArrive/{departureDest}/{arrivalDest}")
    public List<RouteListingSimpleJson> getAllRouteListingsByDepartAndArrive(@PathVariable String departureDest, @PathVariable String arrivalDest) {
        return service.getAllRouteListingsWithDepartureDestAndArrivalDestination(departureDest, arrivalDest);
    }

    @GetMapping(path = "/routeListings/fullSearch/{departureDest}/{arrivalDest}/{year}/{month}/{day}")
    public List<RouteListingSimpleJson> getAllRoutesMatchingFullSearch(
            @PathVariable String departureDest,
            @PathVariable String arrivalDest,
            @PathVariable Integer year,
            @PathVariable Integer month,
            @PathVariable Integer day) {
        // TODO: Figure out how to filter by matching DATE component only for the sql side.
        // For now we will just do in on Spring Boot side.
        LocalDate matchingDate = LocalDate.of(year, month, day);
        // TODO: Figure out why the returned JSON is having it's time component squashed!
        return service.getAllRouteListingsMatchingFullSearch(departureDest, arrivalDest, matchingDate);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/routeListings/new")
    public RouteListing createRouteListing(@Valid @RequestBody RouteListingSimpleJson newRouteListingSimpleJson) {
        try {
            return service.createRouteListing(newRouteListingSimpleJson);
        } catch (Exception e) {
            throw new RouteListingBadRequestException(e);
        }
    }
}
