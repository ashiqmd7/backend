package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.entities.Plane;
import com.G2T5203.wingit.entities.Route;
import com.G2T5203.wingit.entities.RouteListing;
import com.G2T5203.wingit.entities.RouteListingPk;
import com.G2T5203.wingit.plane.PlaneNotFoundException;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteNotFoundException;
import com.G2T5203.wingit.route.RouteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteListingService {
    private final RouteListingRepository repo;
    private final RouteRepository routeRepo;
    private final PlaneRepository planeRepo;

    public RouteListingService(RouteListingRepository repo, RouteRepository routeRepo, PlaneRepository planeRepo) {
        this.repo = repo;
        this.routeRepo = routeRepo;
        this.planeRepo = planeRepo;
    }

    public List<RouteListingSimpleJson> getAllRouteListings() {
        List<RouteListing> routeListings = repo.findAll();
        return routeListings.stream()
                .map(RouteListingSimpleJson::new)
                .collect(Collectors.toList());
    }

    public List<RouteListingSimpleJson> getAllRouteListingsWithDepartureDest(String departureDest) {
        List<RouteListing> routeListings = repo.findByRouteListingPkRouteDepartureDest(departureDest);
        return routeListings.stream()
                .map(RouteListingSimpleJson::new)
                .collect(Collectors.toList());
    }

    public List<RouteListingSimpleJson> getAllRouteListingsWithDepartureDestAndArrivalDestination(String departureDest, String arrivalDest) {
        List<RouteListing> routeListings = repo.findByRouteListingPkRouteDepartureDestAndRouteListingPkRouteArrivalDest(departureDest, arrivalDest);
        return routeListings.stream()
                .map(RouteListingSimpleJson::new)
                .collect(Collectors.toList());
    }

    public List<RouteListingSimpleJson> getAllRouteListingsMatchingFullSearch(String departureDest, String arrivalDest, LocalDate matchingDate) {
        List<RouteListing> routeListings = repo.findByRouteListingPkRouteDepartureDestAndRouteListingPkRouteArrivalDest(departureDest, arrivalDest);
        return routeListings.stream().filter(routeListing -> {
            LocalDate routeListingDate = routeListing.getRouteListingPk().getDepartureDatetime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            return routeListingDate.equals(matchingDate);
        }).map(RouteListingSimpleJson::new).collect(Collectors.toList());
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
