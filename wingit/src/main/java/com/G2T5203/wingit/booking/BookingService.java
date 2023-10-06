package com.G2T5203.wingit.booking;

import com.G2T5203.wingit.entities.*;
import com.G2T5203.wingit.plane.PlaneNotFoundException;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteNotFoundException;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListingNotFoundException;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.routeListing.RouteListingService;
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
    private final RouteListingService routeListingService;

    public BookingService(
            BookingRepository repo,
            UserRepository userRepo,
            PlaneRepository planeRepo,
            RouteRepository routeRepo,
            RouteListingRepository routeListingRepo,
            SeatListingService seatListingService,
            RouteListingService routeListingService) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.planeRepo = planeRepo;
        this.routeRepo = routeRepo;
        this.routeListingRepo = routeListingRepo;
        this.seatListingService = seatListingService;
        this.routeListingService = routeListingService;
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
        // Checking for expired bookings.
        boolean hasDeletedSomeExpiredBookings = false;
        for (Booking booking : bookings) {
            if (isBookingExpired(booking)) {
                forceDeleteBooking(booking);
                hasDeletedSomeExpiredBookings = true;
            }
        }
        if (hasDeletedSomeExpiredBookings) {
            bookings = repo.findAllByWingitUserUsername(username);
        }

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

        // Check if routeListing has enough seatListings for Booking's partySize
        int bookingPax = bookingSimpleJson.getPartySize();
        int remainingSeatsForRouteListing = routeListingService.calculateRemainingSeatsForRouteListing(thisOutboundRouteListingPk);
        if (remainingSeatsForRouteListing < bookingPax) {
            throw new BookingBadRequestException("Routelisting has insufficient seats for selected pax");
        }


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

        // Check date of InboundRouteListing. If it is before OutboundRouteListing, throw exception
        // Do this by retrieving the outboundRouteListing's departureDatetime & compare with the inboundRouteListing departureDatetime
        LocalDateTime outboundDatetime = retrievedBooking.get().getOutboundRouteListing().getRouteListingPk().getDepartureDatetime();
        if (inboundDepartureDatetime.isBefore(outboundDatetime)) {
            throw new BookingBadRequestException("Inbound flight departure datetime is before outbound");
        }

        retrievedBooking.get().setInboundRouteListing(retrievedInboundRouteListing.get());
        return repo.save(retrievedBooking.get());
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
            if (isBookingExpired(booking)) {
                forceDeleteBooking(booking);
            } else {
                activeUnfinishedBookings.add(booking);
            }
        }

        return activeUnfinishedBookings;
    }

    private boolean isBookingExpired(Booking booking) {
        if (booking.isPaid()) return false;
        final int MAX_DURATION_IN_MINUTES = 15;
        boolean isPastExpiry = Duration.between(booking.getStartBookingDatetime(), LocalDateTime.now()).toMinutes() > MAX_DURATION_IN_MINUTES;
        // Only expired if it's not paid and past the timings.
        return isPastExpiry && !booking.isPaid();
    }

    @Transactional
    public double calculateAndSaveChargedPrice(int bookingId) {
        Optional<Booking> bookingOptional = repo.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            // TODO: Check if we should be expecting empty or 0 charged price? Cause we shouldn't be calculating if already have...?

            // TODO: ALSO need to check if the total number of seats is correct! If pax is 5, with both inbout and outbound, should expect 10 seats total.
            // hihi
            double outboundPriceTotal = 0.0;
            double inboundPriceTotal = 0.0;
            final double outboundBasePrice = booking.getOutboundRouteListing().getBasePrice();
            final double inboundBasePrice = booking.hasInboundRouteListing() ? booking.getInboundRouteListing().getBasePrice() : 0.0;
            for (SeatListing seatListing : booking.getSeatListing()) {
                boolean isOutboundRouteListing = seatListing.getSeatListingPk().getRouteListing().getRouteListingPk() == booking.getOutboundRouteListing().getRouteListingPk();
                if (isOutboundRouteListing) {
                    outboundPriceTotal += seatListing.getSeatListingPk().getSeat().getPriceFactor() * outboundBasePrice;
                } else {
                    inboundPriceTotal += seatListing.getSeatListingPk().getSeat().getPriceFactor() * inboundBasePrice;
                }
            }
            double totalChargedPrice = outboundPriceTotal + inboundPriceTotal;
            booking.setChargedPrice(totalChargedPrice);
            repo.save(booking);

            return totalChargedPrice;
        } else {
            throw new BookingNotFoundException(bookingId);
        }
    }

    @Transactional
    public void markBookingAsPaid(int bookingId) {
        Optional<Booking> bookingOptional = repo.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            // TODO: Maybe some logic checks here....? With stripe. Not sure how it works yet.

            // TODO: ALSO!!! Need to check here that booking is fully booked. i.e. all seatlistings are filled correctly.
            booking.setPaid(true);
            repo.save(booking);
        } else {
            throw new BookingNotFoundException(bookingId);
        }
    }
}
