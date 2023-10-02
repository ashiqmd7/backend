package com.G2T5203.wingit.booking;

import com.G2T5203.wingit.entities.*;
import com.G2T5203.wingit.plane.PlaneNotFoundException;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteNotFoundException;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListingNotFoundException;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.seatListing.SeatListingService;
import com.G2T5203.wingit.user.UserNotFoundException;
import com.G2T5203.wingit.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository repo;
    private final UserRepository userRepo;
    private final PlaneRepository planeRepo;
    private final RouteRepository routeRepo;
    private final RouteListingRepository routeListingRepo;
    private final SeatListingService seatListingService;

    public BookingService(
            BookingRepository repo,
            UserRepository userRepo,
            PlaneRepository planeRepo,
            RouteRepository routeRepo,
            RouteListingRepository routeListingRepo,
            SeatListingService seatListingService) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.planeRepo = planeRepo;
        this.routeRepo = routeRepo;
        this.routeListingRepo = routeListingRepo;
        this.seatListingService = seatListingService;
    }

    public List<BookingSimpleJson> getAllBookings() {
        List<Booking> bookings = repo.findAll();
        return bookings.stream()
                .map(BookingSimpleJson::new)
                .collect(Collectors.toList());
    }

    // Get all the bookings under a user
    public List<BookingSimpleJson> getAllBookingsByUser(String username) {
        List<Booking> bookings = repo.findAllByWingitUserUsername(username);
        return bookings.stream()
                .map(BookingSimpleJson::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Booking createBooking(BookingSimpleJson bookingSimpleJson) {
        Optional<WingitUser> retrievedUser = userRepo.findByUsername(bookingSimpleJson.getUsername());
        if (retrievedUser.isEmpty()) throw new UserNotFoundException(bookingSimpleJson.getUsername());

        // For outbound, get Plane, Route, and departureDatetime to form RouteListingPk
        // Afterwards use RouteListingPk to find RouteListing
        Optional<Plane> retrievedOutboundPlane = planeRepo.findById(bookingSimpleJson.getOutboundPlaneId());
        if (retrievedOutboundPlane.isEmpty()) throw new PlaneNotFoundException(bookingSimpleJson.getOutboundPlaneId());

        Optional<Route> retrievedOutboundRoute = routeRepo.findById(bookingSimpleJson.getOutboundRouteId());
        if (retrievedOutboundRoute.isEmpty()) throw new RouteNotFoundException(bookingSimpleJson.getOutboundRouteId());

        RouteListingPk thisOutboundRouteListingPk = new RouteListingPk(retrievedOutboundPlane.get(), retrievedOutboundRoute.get(), bookingSimpleJson.getOutboundDepartureDatetime());
        Optional<RouteListing> retrievedOutboundRouteListing = routeListingRepo.findById(thisOutboundRouteListingPk);
        if (retrievedOutboundRouteListing.isEmpty()) throw new RouteListingNotFoundException(thisOutboundRouteListingPk);

        Booking newBooking = new Booking(
                retrievedUser.get(),
                retrievedOutboundRouteListing.get(),
                null,
                bookingSimpleJson.getStartBookingDatetime(),
                bookingSimpleJson.getPartySize(),
                bookingSimpleJson.getChargedPrice(),
                false
        );

        return repo.save(newBooking);
    }

    // PUT to update for inbound
    @Transactional
    public Booking updateInboundBooking(int bookingId, String inboundPlaneId, int inboundRouteId, LocalDateTime inboundDepartureDatetime) {
        Optional<Booking> retrievedBooking = repo.findById(bookingId);
        if (retrievedBooking.isEmpty()) throw new BookingNotFoundException(bookingId);

        // For inbound, get Plane, Route, and departureDatetime to form RouteListingPk
        // Afterwards use RouteListingPk to find RouteListing
        Optional<Plane> retrievedInboundPlane = planeRepo.findById(inboundPlaneId);
        if (retrievedInboundPlane.isEmpty()) throw new PlaneNotFoundException(inboundPlaneId);

        Optional<Route> retrievedInboundRoute = routeRepo.findById(inboundRouteId);
        if (retrievedInboundRoute.isEmpty()) throw new RouteNotFoundException(inboundRouteId);

        RouteListingPk thisInboundRouteListingPk = new RouteListingPk(retrievedInboundPlane.get(), retrievedInboundRoute.get(), inboundDepartureDatetime);
        Optional<RouteListing> retrievedInboundRouteListing = routeListingRepo.findById(thisInboundRouteListingPk);
        if (retrievedInboundRouteListing.isEmpty()) throw new RouteListingNotFoundException(thisInboundRouteListingPk);

        retrievedBooking.get().setInboundRouteListing(retrievedInboundRouteListing.get());
        return repo.save(retrievedBooking.get());
    }

    // PUT to update isPaid after payment
    @Transactional
    public Booking updateIsPaid(int bookingId, boolean paymentStatus) {
        Optional<Booking> retrieveBooking = repo.findById(bookingId);
        if (retrieveBooking.isEmpty()) throw new BookingNotFoundException(bookingId);

        retrieveBooking.get().setPaid(paymentStatus);
        return repo.save(retrieveBooking.get());
    }

    @Transactional
    public void deleteBookingById(int bookingId) {
        if (repo.existsById(bookingId)) {
            repo.deleteById((bookingId));
        } else {
            throw new BookingNotFoundException(bookingId);
        }
    }

    @Transactional
    private void forceDeleteBookingById(int bookingId) {
        Optional<Booking> booking = repo.findById(bookingId);
        if (booking.isPresent()) {
            forceDeleteBooking(booking.get());
        } else {
            throw new BookingNotFoundException(bookingId);
        }
    }

    @Transactional
    private void forceDeleteBooking(Booking booking) {
        try {
            for (SeatListing seatListing : booking.getSeatListing()) {
                // Delete all seatListings if any.
                RouteListingPk routeListingPk = seatListing.getSeatListingPk().getRouteListing().getRouteListingPk();
                seatListingService.cancelSeatListingBooking(
                        routeListingPk.getPlane().getPlaneId(),
                        routeListingPk.getRoute().getRouteId(),
                        routeListingPk.getDepartureDatetime(),
                        seatListing.getSeatListingPk().getSeat().getSeatPk().getSeatNumber());
            }
            repo.deleteById((booking.getBookingId()));
        } catch (Exception e) {
            throw new BookingBadRequestException(e);
        }
    }

    @Transactional
    public List<Booking> getActiveUnfinishedBookingsForRouteListing(RouteListingPk routeListingPk) {
        List<Booking> matchingUnfinishedOutboundRouteListing = repo.findAllByOutboundRouteListingRouteListingPkAndIsPaidFalse(routeListingPk);
        List<Booking> activeUnfinishedBookings = new ArrayList<>();
        for (Booking booking : matchingUnfinishedOutboundRouteListing) {
            // TODO: Sort through those "expired" and delete them.
            final int MAX_DURATION_IN_MINUTES = 15;
            boolean isAfter15Minutes = Duration.between(booking.getStartBookingDatetime(), LocalDateTime.now()).toMinutes() > MAX_DURATION_IN_MINUTES;
            if (isAfter15Minutes) {
                forceDeleteBooking(booking);
            } else {
                activeUnfinishedBookings.add(booking);
            }
        }

        return activeUnfinishedBookings;
    }
}
