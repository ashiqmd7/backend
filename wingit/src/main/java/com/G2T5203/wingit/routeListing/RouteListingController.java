package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.entities.RouteListing;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class RouteListingController {
    private final RouteListingService service;

    public RouteListingController(RouteListingService service) { this.service = service; }

    @GetMapping(path = "/routeListings")
    public List<RouteListingSimpleJson> getAllRouteListings() { return service.getAllRouteListings(); }

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
