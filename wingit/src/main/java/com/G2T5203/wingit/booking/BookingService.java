package com.G2T5203.wingit.booking;

import com.G2T5203.wingit.plane.Plane;
import com.G2T5203.wingit.plane.PlaneNotFoundException;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.Route;
import com.G2T5203.wingit.route.RouteNotFoundException;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListing;
import com.G2T5203.wingit.routeListing.RouteListingNotFoundException;
import com.G2T5203.wingit.routeListing.RouteListingPk;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.seatListing.SeatListing;
import com.G2T5203.wingit.seatListing.SeatListingRepository;
import com.G2T5203.wingit.seatListing.SeatListingService;
import com.G2T5203.wingit.user.UserNotFoundException;
import com.G2T5203.wingit.user.UserRepository;
import com.G2T5203.wingit.user.WingitUser;
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
    private final SeatListingRepository seatListingRepo;

    public BookingService(BookingRepository repo,
                          UserRepository userRepo,
                          PlaneRepository planeRepo,
                          RouteRepository routeRepo,
                          RouteListingRepository routeListingRepo,
                          SeatListingService seatListingService,
                          SeatListingRepository seatListingRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.planeRepo = planeRepo;
        this.routeRepo = routeRepo;
        this.routeListingRepo = routeListingRepo;
        this.seatListingService = seatListingService;
        this.seatListingRepo = seatListingRepo;
    }

    // get username of booking's user using bookingId
    public String getBookingUserUsername(int bookingId) {
        Optional<Booking> optionalRetrievedBooking = repo.findById(bookingId);
        if (optionalRetrievedBooking.isEmpty()) throw new BookingNotFoundException(bookingId);
        Booking retrievedBooking = optionalRetrievedBooking.get();
        if (isBookingExpired(retrievedBooking)) {
            forceDeleteBooking(retrievedBooking);
            throw new BookingExpiredException();
        }

        return retrievedBooking.getWingitUser().getUsername();
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

    // Needed to copy over this function from RouteListingService due to circular dependency error
    // (bookingService uses routeListingService uses bookingService)
    // Also makes sense for this to be here since we are grabbing a lot of data from the bookings as well to calculate
    // those currently booking.
    public int calculateRemainingSeatsForRouteListing(RouteListingPk routeListingPk) {
        List<SeatListing> availableSeats = seatListingRepo.findBySeatListingPkRouteListingRouteListingPkAndBookingIsNull(routeListingPk);
        List<Booking> activeBookingsForRouteListing = getActiveUnfinishedBookingsForRouteListing(routeListingPk);
        int numRemainingSeats = availableSeats.size();
        for (Booking booking : activeBookingsForRouteListing) {
            numRemainingSeats -= booking.getPartySize(); // Remove reserved number of seats for active booking
            numRemainingSeats += booking.getSeatListing().size(); // Add back to tally those they already booked to undo doublecount.
        }

        return numRemainingSeats;
    }

    @Transactional
    public BookingSimpleJson createBooking(BookingSimpleJson bookingSimpleJson) {
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
        int remainingSeatsForRouteListing = calculateRemainingSeatsForRouteListing(thisOutboundRouteListingPk);
        if (remainingSeatsForRouteListing < bookingPax) {
            throw new BookingBadRequestException("Routelisting has insufficient seats for selected pax");
        }


        // When creating, always force isPaid to false and chargedPrice as -1 to indicate that they are not yet set.
        bookingSimpleJson.setChargedPrice(-1.0);
        bookingSimpleJson.setPaid(false);

        // also always override startDateTime with server side LocalDateTime.
        bookingSimpleJson.setStartBookingDatetime(LocalDateTime.now());
        Booking newBooking = new Booking(
                retrievedUser.get(),
                retrievedOutboundRouteListing.get(),
                null,
                bookingSimpleJson.getStartBookingDatetime(),
                bookingSimpleJson.getPartySize(),
                bookingSimpleJson.getChargedPrice(),
                false
        );

        repo.save(newBooking);

        return new BookingSimpleJson(newBooking);
    }

    // PUT to update for inbound
    @Transactional
    public BookingSimpleJson updateInboundBooking(int bookingId, String inboundPlaneId, int inboundRouteId, LocalDateTime inboundDepartureDatetime) {
        Optional<Booking> retrievedBooking = repo.findById(bookingId);
        if (retrievedBooking.isEmpty()) throw new BookingNotFoundException(bookingId);
        if (isBookingExpired(retrievedBooking.get())) {
            forceDeleteBooking(retrievedBooking.get());
            throw new BookingExpiredException();
        }

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
        repo.save(retrievedBooking.get());

        return new BookingSimpleJson(retrievedBooking.get());
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

    private void throwIfSeatBookingsIncomplete(Booking booking) throws BookingBadRequestException {
        // retrieve outbound & inbound routeListings
        // count number of seatListing's routeListingPks that matches outboundRoutelistingPk
        // count number of seatListing's routeListingPks that matches inboundRoutelistingPk
        // check if count == count == booking's partySize
        RouteListingPk outboundRouteListingPk = booking.getOutboundRouteListing().getRouteListingPk();
        int outboundSeatListingCount = 0;
        for (SeatListing seatListing : booking.getSeatListing()) {
            if (seatListing.getSeatListingPk().checkSeatBelongsToRouteListing(seatListing, outboundRouteListingPk)) {
                outboundSeatListingCount++;
            }
        }
        if (outboundSeatListingCount != booking.getPartySize()) {
            throw new BookingBadRequestException("Incorrect number of seats booked for partySize.");
        }
        if (booking.hasInboundRouteListing()) {
            RouteListingPk inboundRouteListingPk = booking.getInboundRouteListing().getRouteListingPk();
            int inboundSeatListingCount = 0;
            for (SeatListing seatListing : booking.getSeatListing()) {
                if (seatListing.getSeatListingPk().checkSeatBelongsToRouteListing(seatListing, inboundRouteListingPk)) {
                    inboundSeatListingCount++;
                }
            }
            if (outboundSeatListingCount != inboundSeatListingCount) {
                throw new BookingBadRequestException("Number of outbound seatListings does not match number of inbound seatListings.");
            }
        }
    }

    @Transactional
    public double calculateAndSaveChargedPrice(int bookingId) {
        Optional<Booking> bookingOptional = repo.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            if (isBookingExpired(booking)) {
                forceDeleteBooking(booking);
                throw new BookingExpiredException();
            }
            // TODO: Shoule we be checking if chargedPrice < 0? Or do we allow multiple recalculations.
            throwIfSeatBookingsIncomplete(booking);

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
            // Since payment might take time here... let's skip the expiry check...?

            // TODO: Logic checks here... if we have time to integrate with stripe.
            //       Not sure how it works yet. When it comes to it then we figure out. But likely some logic here.
            Booking booking = bookingOptional.get();
            throwIfSeatBookingsIncomplete(booking);

            booking.setPaid(true);
            repo.save(booking);
        } else {
            throw new BookingNotFoundException(bookingId);
        }
    }
}
