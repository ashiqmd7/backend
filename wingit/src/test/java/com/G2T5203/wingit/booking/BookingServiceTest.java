package com.G2T5203.wingit.booking;

import com.G2T5203.wingit.TestUtils;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListingPk;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.routeListing.RouteListingService;
import com.G2T5203.wingit.seatListing.SeatListing;
import com.G2T5203.wingit.seatListing.SeatListingRepository;
import com.G2T5203.wingit.seatListing.SeatListingService;
import com.G2T5203.wingit.user.WingitUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private RouteListingRepository routeListingRepo;
    @Mock
    private SeatListingRepository seatListingRepo;
    @Mock
    private BookingRepository bookingRepo;
    @Mock
    private RouteRepository routeRepo;
    @Mock
    private PlaneRepository planeRepo;
    @Mock
    private RouteListingService routeListingService;
    @Mock
    private SeatListingService seatListingService;
    @InjectMocks
    private BookingService bookingService;

    @LocalServerPort
    private int port;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    TestUtils testUtils;

    @BeforeEach
    void setUp() {
        testUtils = new TestUtils(port, encoder);
    }

    @Test
    void getBookingUserUsername_Return_Name() {
        // arrange
        Booking sampleBooking = testUtils.createSampleBooking1();

        // mock
        when(bookingRepo.findById(any(Integer.class))).thenReturn(Optional.of(sampleBooking));

        // act
        String username = bookingService.getBookingUserUsername(sampleBooking.getBookingId());

        // assert
        assertEquals(sampleBooking.getWingitUser().getUsername(), username);

        // verify
        verify(bookingRepo).findById(sampleBooking.getBookingId());
    }

    @Test
    void getAllBookingsByUser_Return_Bookings() {
        // arrange
        WingitUser sampleUser = testUtils.createSampleUser1();
        Booking sampleBooking1 = testUtils.createSampleBooking1();
        Booking sampleBooking2 = testUtils.createSampleBooking2();
        List<Booking> sampleBookings = new ArrayList<>();
        sampleBookings.add(sampleBooking1);
        sampleBookings.add(sampleBooking2);

        // mock
        when(bookingRepo.findAllByWingitUserUsername(any(String.class))).thenReturn(sampleBookings);

        // act
        List<BookingSimpleJson> bookings = bookingService.getAllBookingsByUser(sampleUser.getUsername());

        // assert
        assertEquals(2, bookings.size());

        // verify
        verify(bookingRepo).findAllByWingitUserUsername(sampleUser.getUsername());
    }

    @Test
    void calculateRemainingSeatsForRouteListing_Return() {
        // arrange
        RouteListingPk sampleRouteListingPk = testUtils.createSampleRouteListingPk1();
        SeatListing sampleSeatListing1 = testUtils.createSampleSeatListing1();
        SeatListing sampleSeatListing2 = testUtils.createSampleSeatListing2();
        List<SeatListing> sampleSeatListings = new ArrayList<>();
        sampleSeatListings.add(sampleSeatListing1);
        sampleSeatListings.add(sampleSeatListing2);

        // mock
        when(seatListingRepo.findBySeatListingPkRouteListingRouteListingPkAndBookingIsNull(any(RouteListingPk.class))).thenReturn(sampleSeatListings);

        // act
        int remainingSeats = bookingService.calculateRemainingSeatsForRouteListing(sampleRouteListingPk);

        // assert
        assertEquals(2, remainingSeats);

        // verify
        verify(seatListingRepo).findBySeatListingPkRouteListingRouteListingPkAndBookingIsNull(sampleRouteListingPk);
    }
}
