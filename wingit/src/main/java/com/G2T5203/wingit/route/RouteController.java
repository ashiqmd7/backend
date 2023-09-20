package com.G2T5203.wingit.route;

import com.G2T5203.wingit.entities.Route;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class RouteController {
    private final RouteService service;

    public RouteController(RouteService service) { this.service = service; }

    @GetMapping(path = "/routes")
    public List<Route> getAllRoutes() { return service.getAllRoutes(); }

    @GetMapping(path = "/routes/departureDest/{departureDest}")
    public List<Route> getAllRoutesWithDepartureDest(@PathVariable String departureDest) {
        return service.getAllRoutesWithDepartureDest(departureDest);
    }

    @GetMapping(path = "/routes/{routeId}")
    public Route getRoute(@PathVariable Integer routeId) {
        Route route = service.getRoute(routeId);
        if (route == null) throw new RouteNotFoundException(routeId);
        return route;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/routes/new")
    public Route createRoute(@Valid @RequestBody Route newRoute) {
        try {
            return service.createRoute(newRoute);
        } catch (Exception e) {
            throw new RouteBadRequestException(e);
        }
    }

    @DeleteMapping(path = "/routes/delete/{routeId}")
    public void deleteRoute(@PathVariable Integer routeId) {
        try {
            service.deleteRoute(routeId);
        } catch (RouteNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RouteBadRequestException(e);
        }
    }

    @PutMapping(path = "/routes/update/{routeId}")
    public Route updateRoute(@PathVariable Integer routeId, @Valid @RequestBody Route updatedRoute) {
        if (routeId.intValue() != updatedRoute.getRouteId().intValue()) throw new RouteBadRequestException("Not the same routeId.");

        updatedRoute.setRouteId(routeId);
        try {
            return service.updateRoute(updatedRoute);
        } catch (RouteNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RouteBadRequestException(e);
        }
    }
}
