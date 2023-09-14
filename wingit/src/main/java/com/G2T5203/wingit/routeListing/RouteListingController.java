package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.entities.RouteListing;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RouteListingController {
    private final RouteListingService service;

    public RouteListingController(RouteListingService service) { this.service = service; }

    @GetMapping(path = "/routeListings")
    public List<RouteListingSimpleJson> getAllRouteListings() { return service.getAllRouteListings(); }

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
