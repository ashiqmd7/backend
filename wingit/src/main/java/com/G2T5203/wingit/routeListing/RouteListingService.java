package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.entities.*;
import com.G2T5203.wingit.plane.PlaneNotFoundException;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteNotFoundException;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.seatListing.SeatListingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteListingService {
    private final RouteListingRepository repo;
    private final RouteRepository routeRepo;
    private final PlaneRepository planeRepo;
    private final SeatListingRepository seatListingRepo;

    public RouteListingService(RouteListingRepository repo, RouteRepository routeRepo, PlaneRepository planeRepo, SeatListingRepository seatListingRepo) {
        this.repo = repo;
        this.routeRepo = routeRepo;
        this.planeRepo = planeRepo;
        this.seatListingRepo = seatListingRepo;
    }

    public List<RouteListingSimpleJson> getAllRouteListings() {
        List<RouteListing> routeListings = repo.findAll();
        return routeListings.stream()
                .map(routeListing -> new RouteListingSimpleJson(routeListing, calculateRemainingSeatsForRouteListing(routeListing.getRouteListingPk())))
                .collect(Collectors.toList());
    }

    public List<RouteListingSimpleJson> getAllRouteListingsWithDepartureDest(String departureDest) {
        List<RouteListing> routeListings = repo.findByRouteListingPkRouteDepartureDest(departureDest);
        return routeListings.stream()
                .map(routeListing -> new RouteListingSimpleJson(routeListing, calculateRemainingSeatsForRouteListing(routeListing.getRouteListingPk())))
                .collect(Collectors.toList());
    }

    public List<RouteListingSimpleJson> getAllRouteListingsWithDepartureDestAndArrivalDestination(String departureDest, String arrivalDest) {
        List<RouteListing> routeListings = repo.findByRouteListingPkRouteDepartureDestAndRouteListingPkRouteArrivalDest(departureDest, arrivalDest);
        return routeListings.stream()
                .map(routeListing -> new RouteListingSimpleJson(routeListing, calculateRemainingSeatsForRouteListing(routeListing.getRouteListingPk())))
                .collect(Collectors.toList());
    }

    public List<RouteListingSimpleJson> getAllRouteListingsMatchingFullSearch(String departureDest, String arrivalDest, LocalDate matchingDate) {
        List<RouteListing> routeListings = repo.findByRouteListingPkRouteDepartureDestAndRouteListingPkRouteArrivalDest(departureDest, arrivalDest);
        return routeListings.stream().filter(routeListing -> {
            LocalDate routeListingDate = routeListing.getRouteListingPk().getDepartureDatetime().toLocalDate();
            return routeListingDate.equals(matchingDate);
        }).map(routeListing -> new RouteListingSimpleJson(routeListing, calculateRemainingSeatsForRouteListing(routeListing.getRouteListingPk()))).collect(Collectors.toList());
    }

    public int calculateRemainingSeatsForRouteListing(RouteListingPk routeListingPk) {
        List<SeatListing> availableSeats = seatListingRepo.findBySeatListingPkRouteListingRouteListingPkAndBookingIsNull(routeListingPk);
        // TODO: Need to implement this!
        //       Next is to check if there are currently processed bookings.
        //       Make sure not to double count also... but not fatal if we do.
        //       So here we remove from the List any with bookingIDs that are unfinished for the routelisting.

        return availableSeats.size();
    }

    @Transactional
    public RouteListing createRouteListing(RouteListingSimpleJson simpleJson) {
        Optional<Route> retrievedRoute = routeRepo.findById(simpleJson.getRouteId());
        if (retrievedRoute.isEmpty()) throw new RouteNotFoundException(simpleJson.getRouteId());

        Optional<Plane> retrievedPlane = planeRepo.findById(simpleJson.getPlaneId());
        if (retrievedPlane.isEmpty()) throw new PlaneNotFoundException(simpleJson.getPlaneId());

        RouteListingPk routeListingPk = new RouteListingPk(
                retrievedPlane.get(),
                retrievedRoute.get(),
                simpleJson.getDepartureDatetime());
        boolean alreadyExists = repo.existsById(routeListingPk);
        if (alreadyExists) throw new RouteListingBadRequestException("RouteListing already exists.");

        RouteListing newRouteListing = new RouteListing(
                routeListingPk,
                simpleJson.getBasePrice());

        return repo.save(newRouteListing);
    }
}
