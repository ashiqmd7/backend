package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.TestUtils;
import com.G2T5203.wingit.booking.BookingRepository;
import com.G2T5203.wingit.booking.BookingService;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListing;
import com.G2T5203.wingit.routeListing.RouteListingPk;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.routeListing.RouteListingService;
import com.G2T5203.wingit.seat.Seat;
import com.G2T5203.wingit.seat.SeatPk;
import com.G2T5203.wingit.seat.SeatRepository;
import com.G2T5203.wingit.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeatListingServiceTest {
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
    private SeatRepository seatRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private RouteListingService routeListingService;
    @InjectMocks
    private SeatListingService seatListingService;
    @Mock
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
    void getAllSeatListingsInRouteListing_Success_Return() {
        // arrange
        RouteListingPk sampleRouteListingPk = testUtils.createSampleRouteListingPk1();

        SeatListing sampleSeatListing1 = testUtils.createSampleSeatListing1();
        SeatListing sampleSeatListing2 = testUtils.createSampleSeatListing2();
        List<SeatListing> seatListingList = new ArrayList<>();
        seatListingList.add(sampleSeatListing1);
        seatListingList.add(sampleSeatListing2);

        // mock
        when(planeRepo.findById(any(String.class))).thenReturn(Optional.of(sampleRouteListingPk.getPlane()));
        when(routeRepo.findById(any(Integer.class))).thenReturn(Optional.of(sampleRouteListingPk.getRoute()));
        when(seatListingRepo.findBySeatListingPkRouteListingRouteListingPk(any(RouteListingPk.class))).thenReturn(seatListingList);

        // act
        List<PrivacySeatListingSimpleJson> privacySeatListingSimpleJsonList = seatListingService.getAllSeatListingsInRouteListing(
                sampleRouteListingPk.getPlane().getPlaneId(),
                sampleRouteListingPk.getRoute().getRouteId(),
                sampleRouteListingPk.getDepartureDatetime());

        // assert
        assertNotNull(privacySeatListingSimpleJsonList);
        assertEquals(seatListingList.size(), privacySeatListingSimpleJsonList.size());

        // verify
        verify(planeRepo).findById(sampleRouteListingPk.getPlane().getPlaneId());
        verify(routeRepo).findById(sampleRouteListingPk.getRoute().getRouteId());
        verify(seatListingRepo).findBySeatListingPkRouteListingRouteListingPk(any(RouteListingPk.class));
    }

    @Test
    void createSeatListing_Success_Return() {
        // arrange
        RouteListing sampleRouteListing = testUtils.createSampleRouteListing1();
        //RouteListingPk sampleRouteListingPk = testUtils.createSampleRouteListingPk1();
        Seat sampleSeat = testUtils.createSampleSeat1();
        SeatListing sampleSeatListing = testUtils.createSampleSeatListing1();
        SeatListingSimpleJson sampleSeatListingSimpleJson = testUtils.createSeatListingSimpleJson();

        // mock
        when(planeRepo.findById(any(String.class))).thenReturn(Optional.of(sampleRouteListing.getRouteListingPk().getPlane()));
        when(routeRepo.findById(any(Integer.class))).thenReturn(Optional.of(sampleRouteListing.getRouteListingPk().getRoute()));
        when(routeListingRepo.findById(any(RouteListingPk.class))).thenReturn(Optional.of(sampleRouteListing));
        when(seatRepo.findById(any(SeatPk.class))).thenReturn(Optional.of(sampleSeat));
        when(seatListingRepo.existsById(any(SeatListingPk.class))).thenReturn(false);
        when(seatListingRepo.save(any(SeatListing.class))).thenAnswer(i -> i.getArguments()[0]);

        // act
        SeatListing seatListing = seatListingService.createSeatListing(sampleSeatListingSimpleJson);

        // assert
        assertNotNull(seatListing);

        // verify
        verify(planeRepo).findById(any(String.class));
        verify(routeRepo).findById(any(Integer.class));
        verify(routeListingRepo).findById(any(RouteListingPk.class));
        verify(seatRepo).findById(any(SeatPk.class));
        verify(seatListingRepo).existsById(any(SeatListingPk.class));
        verify(seatListingRepo).save(any(SeatListing.class));



    }




}
