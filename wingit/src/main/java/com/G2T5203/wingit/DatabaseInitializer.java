package com.G2T5203.wingit;

import com.G2T5203.wingit.booking.BookingRepository;
import com.G2T5203.wingit.booking.BookingService;
import com.G2T5203.wingit.entities.*;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.seat.SeatRepository;
import com.G2T5203.wingit.seatListing.SeatListingRepository;
import com.G2T5203.wingit.seatListing.SeatListingService;
import com.G2T5203.wingit.seatListing.SeatListingSimpleJson;
import com.G2T5203.wingit.user.UserRepository;
import com.G2T5203.wingit.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.awt.print.Book;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Class is used only in DEV profile to pre-populate it with data for testing purposes.

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static void Log(String msg) { logger.debug(msg); }

    private static void initialiseSampleUsers(List<WingitUser> list, UserRepository repo, BCryptPasswordEncoder encoder) {
        list.add(repo.save(new WingitUser(
                "brandonDaddy",
                encoder.encode("goodpassword"),
                "ROLE_USER",
                "Brandon",
                "Choy",
                LocalDate.parse("2001-12-04"),
                "brandon.choy.2037@scis.smu.edu.sg",
                "+65 8746 3847",
                "Mr")));
        list.add(repo.save(new WingitUser(
                "DaddyChoy",
                encoder.encode("password"),
                "ROLE_USER",
                "Jared",
                "Hong",
                LocalDate.parse("1996-10-03"),
                "jared.hong.2034@scis.smu.edu.sg",
                "+65 8455 0750",
                "Mrs")));
        list.add(repo.save(new WingitUser(
                "admin",
                encoder.encode("pass"),
                "ROLE_ADMIN",
                "admin",
                "admin",
                LocalDate.parse("2000-01-01"),
                "admin@admin.com",
                "+65 6475 3846",
                "Master")));
        list.add(repo.save(new WingitUser(
                "richman",
                encoder.encode("iamrich"),
                "ROLE_USER",
                "Richmond",
                "Musk",
                LocalDate.parse("1960-04-02"),
                "rich@richmond.com",
                "+65 8888 8888",
                "Mr")));
    }
    private static void initialiseSamplePlanes(List<Plane> list, PlaneRepository repo) {
        list.add(repo.save(new Plane( "SQ123", 42, "B777")));
        list.add(repo.save(new Plane( "SQ888", 30, "A350")));
        list.add(repo.save(new Plane( "SQ364", 42, "B777")));
        list.add(repo.save(new Plane( "SQ39", 30, "A350")));
        list.add(repo.save(new Plane( "SQ28", 42, "B777")));
        list.add(repo.save(new Plane( "SQ493", 30, "A350")));
        list.add(repo.save(new Plane( "SQ789", 42, "B777")));
        list.add(repo.save(new Plane( "SQ394", 30, "A350")));
        list.add(repo.save(new Plane( "SQ347", 42, "B777")));
        list.add(repo.save(new Plane( "SQ977", 30, "A350")));

        list.add(repo.save(new Plane( "SQ124", 42, "B777")));
        list.add(repo.save(new Plane( "SQ808", 30, "A350")));
        list.add(repo.save(new Plane( "SQ563", 42, "B777")));
        list.add(repo.save(new Plane( "SQ381", 30, "A350")));
        list.add(repo.save(new Plane( "SQ293", 42, "B777")));
        list.add(repo.save(new Plane( "SQ593", 30, "A350")));
        list.add(repo.save(new Plane( "SQ987", 42, "B777")));
        list.add(repo.save(new Plane( "SQ333", 30, "A350")));
        list.add(repo.save(new Plane( "SQ352", 42, "B777")));
        list.add(repo.save(new Plane( "SQ779", 30, "A350")));


        list.add(repo.save(new Plane( "SQ21", 42, "B777")));
        list.add(repo.save(new Plane( "SQ800", 30, "A350")));
        list.add(repo.save(new Plane( "SQ362", 42, "B777")));
        list.add(repo.save(new Plane( "SQ390", 30, "A350")));
        list.add(repo.save(new Plane( "SQ280", 42, "B777")));
        list.add(repo.save(new Plane( "SQ492", 30, "A350")));
        list.add(repo.save(new Plane( "SQ787", 42, "B777")));
        list.add(repo.save(new Plane( "SQ360", 30, "A350")));
        list.add(repo.save(new Plane( "SQ340", 42, "B777")));
        list.add(repo.save(new Plane( "SQ979", 30, "A350")));

        list.add(repo.save(new Plane( "SQ121", 42, "B777")));
        list.add(repo.save(new Plane( "SQ87", 30, "A350")));
        list.add(repo.save(new Plane( "SQ568", 42, "B777")));
        list.add(repo.save(new Plane( "SQ380", 30, "A350")));
        list.add(repo.save(new Plane( "SQ290", 42, "B777")));
        list.add(repo.save(new Plane( "SQ595", 30, "A350")));
        list.add(repo.save(new Plane( "SQ989", 42, "B777")));
        list.add(repo.save(new Plane( "SQ343", 30, "A350")));
        list.add(repo.save(new Plane( "SQ350", 42, "B777")));
        list.add(repo.save(new Plane( "SQ777", 30, "A350")));
    }
    private static void initialiseSampleSeats(List<Seat> list, SeatRepository repo, List<Plane> planeList) {
        for (Plane plane : planeList) {
            int numNonEconomySeats = 0;

            // First class (row 1 to 3) 3 rows, 2 seats per row, 6 seats
            for (int row = 1; row <= 3; row++) {
                for (int seatAlphabet = 0; seatAlphabet < 2; seatAlphabet++) {
                    Character seatChar = (char) ('A' + seatAlphabet);
                    String seatNumber = seatChar + String.format("%02d", row);
                    list.add(repo.save(new Seat(new SeatPk(plane, seatNumber), "First", 10.0)));
                    numNonEconomySeats++;
                }
            }

            // Business class (row 4 to 6) 3 rows, 4 seats per row, 12 seats
            for (int row = 4; row <= 6; row++) {
                for (int seatAlphabet = 0; seatAlphabet < 4; seatAlphabet++) {
                    Character seatChar = (char) ('A' + seatAlphabet);
                    String seatNumber = seatChar + String.format("%02d", row);
                    list.add(repo.save(new Seat(new SeatPk(plane, seatNumber), "Business", 3.0)));
                    numNonEconomySeats++;
                }
            }

            // Economy class (row 7 onwards), 6 seats per row.
            int numEconomyRows = (plane.getCapacity() - numNonEconomySeats) / 6;
            int numEconomySeats = 0;
            for (int row = 7; row <= 7 + numEconomyRows; row++) {
                for (int seatAlphabet = 0; seatAlphabet < 6; seatAlphabet++) {
                    Character seatChar = (char) ('A' + seatAlphabet);
                    String seatNumber = seatChar + String.format("%02d", row);
                    list.add(repo.save(new Seat(new SeatPk(plane, seatNumber), "Economy", 1.0)));
                    numEconomySeats++;
                }
            }

            assert(plane.getCapacity() == (numNonEconomySeats + numEconomySeats));
        }
    }
    private static void initialiseSampleRoutes(List<Route> list, RouteRepository repo) {
        list.add(repo.save(new Route(
                "Singapore",
                "Taiwan",
                Duration.ofHours(4).plusMinutes(45) )));
        list.add(repo.save(new Route(
                "Singapore",
                "Japan",
                Duration.ofHours(7).plusMinutes(10) )));
        list.add(repo.save(new Route(
                "Singapore",
                "China",
                Duration.ofHours(6).plusMinutes(10) )));
        list.add(repo.save(new Route(
                "Singapore",
                "India",
                Duration.ofHours(5).plusMinutes(35) )));

        list.add(repo.save(new Route(
                "Taiwan",
                "Singapore",
                Duration.ofHours(4).plusMinutes(50) )));
        list.add(repo.save(new Route(
                "Taiwan",
                "Japan",
                Duration.ofHours(2).plusMinutes(40) )));
        list.add(repo.save(new Route(
                "Taiwan",
                "China",
                Duration.ofHours(1).plusMinutes(50) )));
        list.add(repo.save(new Route(
                "Taiwan",
                "India",
                Duration.ofHours(12).plusMinutes(25) )));

        list.add(repo.save(new Route(
                "Japan",
                "Singapore",
                Duration.ofHours(7).plusMinutes(15) )));
        list.add(repo.save(new Route(
                "Japan",
                "Taiwan",
                Duration.ofHours(2).plusMinutes(35) )));
        list.add(repo.save(new Route(
                "Japan",
                "China",
                Duration.ofHours(3).plusMinutes(30) )));
        list.add(repo.save(new Route(
                "Japan",
                "India",
                Duration.ofHours(10).plusMinutes(10) )));

        list.add(repo.save(new Route(
                "China",
                "Singapore",
                Duration.ofHours(6).plusMinutes(15) )));
        list.add(repo.save(new Route(
                "China",
                "Taiwan",
                Duration.ofHours(1).plusMinutes(40) )));
        list.add(repo.save(new Route(
                "China",
                "Japan",
                Duration.ofHours(3).plusMinutes(40) )));
        list.add(repo.save(new Route(
                "China",
                "India",
                Duration.ofHours(12).plusMinutes(55) )));

        list.add(repo.save(new Route(
                "India",
                "Singapore",
                Duration.ofHours(5).plusMinutes(40) )));
        list.add(repo.save(new Route(
                "India",
                "Taiwan",
                Duration.ofHours(12).plusMinutes(35) )));
        list.add(repo.save(new Route(
                "India",
                "Japan",
                Duration.ofHours(10).plusMinutes(15) )));
        list.add(repo.save(new Route(
                "India",
                "China",
                Duration.ofHours(12).plusMinutes(45) )));
    }
    private static void initialiseSampleRouteListings(List<RouteListing> list, RouteListingRepository repo, List<Plane> planeList, List<Route> routeList) {
        for (int year = 2023; year <= 2023; year++) {
            for (int month = 10; month <= 12; month++) {
                int daysInMonth;
                if (month == 2) daysInMonth = 28;
                else if (month == 4 || month == 6 || month == 9 || month == 11) daysInMonth = 30;
                else daysInMonth = 31;
                for (int day = 1; day <= daysInMonth; day++) {
                    // NOTE: We have equal double number of planes and routes. Two planes serves each route for sample database.
                    // NOTE: 40 planes, 20 routes. Each Route has two planes flying that route per day.
                    for (int planeOffset = 0; planeOffset < 2; planeOffset++) {
                        for (int i = 0; i < routeList.size(); i++) {
                            int hour = i % 2 == 0 ? 7 : 13;
                            hour += planeOffset;
                            int minute = 15 * (i % 4);
                            minute += planeOffset * 3;
                            String datetimeStr = String.format("%d-%02d-%02d %02d:%02d:00", year, month, day, hour, minute);

                            double price = 500.0;
                            Route route = routeList.get(i);
                            price += route.getFlightDuration().toMinutes() * 0.374 + 7.34 * minute;
                            list.add(repo.save(new RouteListing(
                                    new RouteListingPk(
                                            planeList.get(i + (planeOffset * 20)),
                                            route,
                                            DateUtils.handledParseDateTime(datetimeStr)),
                                    price
                            )));
                        }
                    }
                }
            }
        }
    }
    private static void initialiseSampleSeatListings(List<SeatListing> list, SeatListingRepository repo, List<RouteListing> routeListingList, List<Seat> seatList) {
        long counter = 0;
        for (RouteListing routeListing : routeListingList) {
            Plane plane = routeListing.getRouteListingPk().getPlane();
            List<Seat> seats = seatList.stream().filter(seat -> seat.getSeatPk().getPlane().getPlaneId().equals(plane.getPlaneId())).toList();
            for (Seat seat : seats) {
                list.add(repo.save(new SeatListing(
                        new SeatListingPk(routeListing, seat),
                        null,
                        null
                )));
            }
            counter++;
            if (counter % 100 == 0 || counter == routeListingList.size())
                Log(String.format("[SeatListing progress... %d/%d", counter, routeListingList.size()));
        }
    }

    private static void initialiseSampleBookings(List<Booking> list, BookingRepository repo, List<WingitUser> userList, List<RouteListing> routeListingList, SeatListingService seatListingService, BookingService bookingService) {
        // Go through each and every routeListing
        // Book maybe half of the tickets... So seeded Randomization of what tickets to book.
        // Book it all under this one mega user

        final long RANDOM_SEED = 777L;
        WingitUser richUser = userList.get(3);
        for (RouteListing routeListing : routeListingList) {
            Random random = new Random(RANDOM_SEED);

            Booking newBooking = repo.save(new Booking(
                    richUser,
                    routeListing,
                    null,
                    DateUtils.handledParseDateTime("2023-09-01 16:30:00"),
                    1,
                    -1,
                    false));
            list.add(newBooking);

            List<SeatListingSimpleJson> seatListings = seatListingService.getAllSeatListingsInRouteListing(
                    routeListing.getRouteListingPk().getPlane().getPlaneId(),
                    routeListing.getRouteListingPk().getRoute().getRouteId(),
                    routeListing.getRouteListingPk().getDepartureDatetime());

            int seatIndex = random.nextInt(seatListings.size());
            SeatListingSimpleJson seatChosen = seatListings.get(seatIndex);

            seatListingService.reserveSeatListing(
                    seatChosen.getPlaneId(),
                    seatChosen.getRouteId(),
                    seatChosen.getDepartureDatetime(),
                    seatChosen.getSeatNumber(),
                    newBooking.getBookingId());
            seatListingService.setOccupantForSeatListing(
                    seatChosen.getPlaneId(),
                    seatChosen.getRouteId(),
                    seatChosen.getDepartureDatetime(),
                    seatChosen.getSeatNumber(),
                    newBooking.getBookingId(),
                    richUser.getFirstName());

            bookingService.calculateAndSaveChargedPrice(newBooking.getBookingId());
            bookingService.markBookingAsPaid(newBooking.getBookingId());
        }
    }

    public static void init(ApplicationContext context) {
        // Get encoder
        BCryptPasswordEncoder encoder = context.getBean(BCryptPasswordEncoder.class);

        // Initialise WingitUsers
        UserRepository userRepository = context.getBean(UserRepository.class);
        List<WingitUser> wingitUserList = new ArrayList<>();
        initialiseSampleUsers(wingitUserList, userRepository, encoder);
//        for (WingitUser wingitUser : wingitUserList) { Log("[Add WingitUser]: " + wingitUser); }
        Log("[Added sample WingitUsers]");


        // Initialise Planes
        PlaneRepository planeRepository = context.getBean(PlaneRepository.class);
        List<Plane> planeList = new ArrayList<>();
        initialiseSamplePlanes(planeList, planeRepository);
//        for (Plane plane : planeList) { Log("[Add Plane]: " + plane); }
        Log("[Added sample Planes]");


        // Initialise Seats
        SeatRepository seatRepository = context.getBean(SeatRepository.class);
        List<Seat> seatList = new ArrayList<>();
        initialiseSampleSeats(seatList, seatRepository, planeList);
//        for (Seat seat : seatList) { Log("[Add Seat]: " + seat); }
        Log("[Added sample Seats]");


        // Initialise Routes
        RouteRepository routeRepository = context.getBean(RouteRepository.class);
        List<Route> routeList = new ArrayList<>();
        initialiseSampleRoutes(routeList, routeRepository);
//        for (Route route : routeList) { Log("[Add Route]: " + route); }
        Log("[Added sample Routes]");


        // Initialise RouteListings
        RouteListingRepository routeListingRepository = context.getBean(RouteListingRepository.class);
        List<RouteListing> routeListingList = new ArrayList<>();
        initialiseSampleRouteListings(routeListingList, routeListingRepository, planeList, routeList);
//        for (RouteListing routeListing : routeListingList) { Log("[Add RouteListing]: " + routeListing); }
        Log("[Added sample RouteListings]");


        // Initialise SeatListings
        SeatListingRepository seatListingRepository = context.getBean(SeatListingRepository.class);
        List<SeatListing> seatListingList = new ArrayList<>();
        initialiseSampleSeatListings(seatListingList, seatListingRepository, routeListingList, seatList);
//        for (SeatListing seatListing : seatListingList) { Log("[Add SeatListing]: " + seatListing); }
        Log("[Added sample SeatListing]");

        // Initialise Bookings
        BookingRepository bookingRepository = context.getBean(BookingRepository.class);
        SeatListingService seatListingService = context.getBean(SeatListingService.class);
        BookingService bookingService = context.getBean(BookingService.class);
        List<Booking> bookingList = new ArrayList<>();
        initialiseSampleBookings(bookingList, bookingRepository, wingitUserList, routeListingList, seatListingService, bookingService);
//        for (Booking booking : bookingList) { Log("[Add Booking]: " + booking); }
        Log("[Added sample Bookings]");

        Log("[Finished Initialising Sample Database Data]");
    }
}
