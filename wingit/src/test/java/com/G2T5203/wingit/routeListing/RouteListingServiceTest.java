package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.TestUtils;
import com.G2T5203.wingit.route.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteListingServiceTest {
    @Mock
    private RouteListingRepository routeListingRepo;

    @InjectMocks
    private RouteListingService routeListingService;

    @LocalServerPort
    private int port;

    private BCryptPasswordEncoder encoder;

    TestUtils testUtils;

    @BeforeEach
    void setUp() {
        testUtils = new TestUtils(port, encoder);
    }

    @Test
    void getAllRouteListings_ReturnAll() {
        routeListingService.getAllRouteListings();
        verify(routeListingRepo).findAll();
    }

    @Test
    void getAllRouteListingsMatchingFullSearch_ReturnRouteListing() {
        RouteListing sampleRouteListing = testUtils.createSampleRouteListing1();
        ArrayList<RouteListing> sampleRouteListingList = new ArrayList<>();
        sampleRouteListingList.add(sampleRouteListing);

        when(routeListingRepo.findByRouteListingPkRouteDepartureDestAndRouteListingPkRouteArrivalDest(any(String.class), any(String.class))).thenReturn(sampleRouteListingList);

        List<RouteListingSimpleJson> routeListings = routeListingService.getAllRouteListingsMatchingFullSearch("Singapore", "Taiwan", LocalDate.of(2023, 12, 17));

        assertNotNull(routeListings);
        verify(routeListingRepo).findByRouteListingPkRouteDepartureDestAndRouteListingPkRouteArrivalDest(any(String.class), any(String.class));
    }

}
