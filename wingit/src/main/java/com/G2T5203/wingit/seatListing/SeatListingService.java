package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.booking.BookingRepository;
import com.G2T5203.wingit.entities.*;
import com.G2T5203.wingit.plane.PlaneNotFoundException;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteNotFoundException;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListingNotFoundException;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.seat.SeatNotFoundException;
import com.G2T5203.wingit.seat.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatListingService {
    private final SeatListingRepository repo;
    private final PlaneRepository planeRepo;
    private final RouteRepository routeRepo;
    private final RouteListingRepository routeListingRepo;
    private final SeatRepository seatRepo;
    private final BookingRepository bookingRepo;


    public SeatListingService(SeatListingRepository repo, PlaneRepository planeRepo, RouteRepository routeRepo, RouteListingRepository routeListingRepo, SeatRepository seatRepo, BookingRepository bookingRepo) {
        this.repo = repo;
        this.planeRepo = planeRepo;
        this.routeRepo = routeRepo;
        this.routeListingRepo = routeListingRepo;
        this.seatRepo = seatRepo;
        this.bookingRepo = bookingRepo;
    }

    public List<SeatListingSimpleJson> getAllSeatListings() {
        List<SeatListing> seatListings = repo.findAll();
        return seatListings.stream()
                .map(SeatListingSimpleJson::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public SeatListing createSeatListing(SeatListingSimpleJson newSeatListingSimpleJson) {

        // First, check if Plane exists
        Optional<Plane> retrievedPlane = planeRepo.findById(newSeatListingSimpleJson.getRoutePlaneId());
        if (retrievedPlane.isEmpty()) throw new PlaneNotFoundException(newSeatListingSimpleJson.getRoutePlaneId());

        // Second, check if Route exists
        Optional<Route> retrievedRoute = routeRepo.findById(newSeatListingSimpleJson.getRouteId());
        if (retrievedRoute.isEmpty()) throw new RouteNotFoundException(newSeatListingSimpleJson.getRouteId());

        // Third, check if RouteListing exists --> Create a RouteListingPk first to check
        RouteListingPk thisRouteListingPk = new RouteListingPk(retrievedPlane.get(), retrievedRoute.get(), newSeatListingSimpleJson.getDepartureDatetime());
        Optional<RouteListing> retrievedRouteListing = routeListingRepo.findById(thisRouteListingPk);
        if (retrievedRouteListing.isEmpty()) throw new RouteListingNotFoundException(thisRouteListingPk);

        // Fourth, check if Seat exists --> Create a Seat first to check
        SeatPk thisSeatPk = new SeatPk(retrievedPlane.get(), newSeatListingSimpleJson.getSeatNumber());
        Optional<Seat> retrievedSeat = seatRepo.findById(thisSeatPk);
        if (retrievedSeat.isEmpty()) throw new SeatNotFoundException(thisSeatPk);

        // By this time we have guaranteed the fields that consist a SeatListingPk exists
        // So create a SeatListingPk to check if it already exists
        SeatListingPk seatListingPk = new SeatListingPk(retrievedRouteListing.get(), retrievedSeat.get());
        boolean alreadyExists = repo.existsById(seatListingPk);
        if (alreadyExists) throw new SeatListingBadRequestException("Seatlisting already exists.");

        // By here, we have guaranteed that we can create a SeatListing
        // So create it and save
        // But remember that when creating a SeatListing, bookingId and occupantName is null
        // Booking updated after payment
        // occupantName updated after inputting details
        SeatListing newSeatListing = new SeatListing(
                seatListingPk,
                null,
                null);
        return repo.save(newSeatListing);
    }
}
