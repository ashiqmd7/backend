package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.booking.BookingNotFoundException;
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

import java.time.LocalDateTime;
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

    public List<SeatListingSimpleJson> getAllSeatListingsInRouteListing(String planeId, int routeId, LocalDateTime departureDateTime) {
        Optional<Plane> retrievedPlane = planeRepo.findById(planeId);
        if (retrievedPlane.isEmpty()) throw new PlaneNotFoundException(planeId);

        Optional<Route> retrievedRoute = routeRepo.findById(routeId);
        if (retrievedRoute.isEmpty()) throw new RouteNotFoundException(routeId);

        RouteListingPk retrievedRoutListingPk = new RouteListingPk(retrievedPlane.get(), retrievedRoute.get(), departureDateTime);
        List<SeatListing> matchingSeatListings = repo.findBySeatListingPkRouteListingRouteListingPk(retrievedRoutListingPk);
        if (matchingSeatListings.isEmpty()) throw new SeatListingNotFoundException("No seatListing with sepcified routeListingPk");

        return matchingSeatListings.stream()
                .map(SeatListingSimpleJson::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public SeatListing createSeatListing(SeatListingSimpleJson newSeatListingSimpleJson) {

        // First, check if Plane exists
        Optional<Plane> retrievedPlane = planeRepo.findById(newSeatListingSimpleJson.getPlaneId());
        if (retrievedPlane.isEmpty()) throw new PlaneNotFoundException(newSeatListingSimpleJson.getPlaneId());

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

    @Transactional
    public SeatListing reserveSeatListing(String planeId, int routeId, LocalDateTime departureDateTime, String seatNumber, Integer bookingId) {
        // Retrieve routeListing, check if exists
        Optional<Plane> retrievedPlane = planeRepo.findById(planeId);
        if (retrievedPlane.isEmpty()) throw new PlaneNotFoundException(planeId);

        Optional<Route> retrievedRoute = routeRepo.findById(routeId);
        if (retrievedRoute.isEmpty()) throw new RouteNotFoundException(routeId);

        RouteListingPk routeListingPk = new RouteListingPk(retrievedPlane.get(), retrievedRoute.get(), departureDateTime);
        Optional<RouteListing> retrievedRouteListing = routeListingRepo.findById(routeListingPk);
        if (retrievedRouteListing.isEmpty()) throw new RouteListingNotFoundException(routeListingPk);

        // Retrieve booking, check if exists
        Optional<Booking> retrievedBooking = bookingRepo.findById(bookingId);
        if (retrievedBooking.isEmpty()) throw new BookingNotFoundException(bookingId);

        // Retrieve booking's seatlisting (list). For each seatListing in it
        // such that the seatListing's routeListing's routeListingPk matches this routeListingPk
        // If matches, count++.
        // Afterwards, check if count < booking's partySize
        List<SeatListing> seatListings = retrievedBooking.get().getSeatListing();
        int count = 0;
        for (SeatListing seatListing : seatListings) {
            if (seatListing.getSeatListingPk().getRouteListing().getRouteListingPk().equals(routeListingPk)) {
                count++;
            }
        }
        
        if (count >= retrievedBooking.get().getPartySize()) {
            throw new SeatListingBadRequestException("Max number of seats have been selected");
        }

        return setSeatListing(planeId, routeId, departureDateTime, seatNumber, bookingId, null);
    }
    @Transactional
    public SeatListing setOccupantForSeatListing(String planeId, int routeId, LocalDateTime departureDateTime, String seatNumber, Integer bookingId, String occupantName) {
        return setSeatListing(planeId, routeId, departureDateTime, seatNumber, bookingId, occupantName);
    }
    @Transactional
    private SeatListing setSeatListing(String planeId, int routeId, LocalDateTime departureDateTime, String seatNumber, Integer bookingId, String occupantName) {
        Optional<Plane> retrievedPlane = planeRepo.findById(planeId);
        if (retrievedPlane.isEmpty()) throw new PlaneNotFoundException(planeId);

        Optional<Route> retrievedRoute = routeRepo.findById(routeId);
        if (retrievedRoute.isEmpty()) throw new RouteNotFoundException(routeId);

        RouteListingPk routeListingPk = new RouteListingPk(retrievedPlane.get(), retrievedRoute.get(), departureDateTime);
        Optional<RouteListing> retrievedRouteListing = routeListingRepo.findById(routeListingPk);
        if (retrievedRouteListing.isEmpty()) throw new RouteListingNotFoundException(routeListingPk);

        SeatPk seatPk = new SeatPk(retrievedPlane.get(), seatNumber);
        Optional<Seat> retrievedSeat = seatRepo.findById(seatPk);
        if (retrievedSeat.isEmpty()) throw new SeatNotFoundException(seatPk);

        SeatListingPk seatListingPk = new SeatListingPk(retrievedRouteListing.get(), retrievedSeat.get());
        Optional<SeatListing> retrievedSeatListing = repo.findById(seatListingPk);
        if (retrievedSeatListing.isEmpty()) throw new SeatListingNotFoundException(seatListingPk);


        SeatListing seatListing = retrievedSeatListing.get();
        if (bookingId != null) {
            Optional<Booking> retrievedBooking = bookingRepo.findById(bookingId);
            if (retrievedBooking.isEmpty()) throw new BookingNotFoundException(bookingId);
            // If bookingId given, check if it matches bookingId from the retrivedBooking
            if (!retrievedBooking.get().getBookingId().equals(bookingId)) {
                throw new SeatListingBadRequestException("Invalid booking ID");
            }
            seatListing.setBooking(retrievedBooking.get());

        } else {
            seatListing.setBooking(null);
        }

        seatListing.setOccupantName(occupantName);
        repo.save(seatListing);

        return seatListing;
    }

    @Transactional
    public SeatListing cancelSeatListingBooking(String planeId, int routeId, LocalDateTime departureDateTime, String seatNumber) {
        return setSeatListing(planeId, routeId, departureDateTime, seatNumber, null, null);
    }
}
