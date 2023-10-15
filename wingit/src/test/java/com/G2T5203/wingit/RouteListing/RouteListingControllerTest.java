package com.G2T5203.wingit.RouteListing;

import com.G2T5203.wingit.TestUtils;
import com.G2T5203.wingit.entities.Route;
import com.G2T5203.wingit.entities.Plane;
import com.G2T5203.wingit.entities.RouteListing;
import com.G2T5203.wingit.entities.RouteListingPk;
import com.G2T5203.wingit.user.UserRepository;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.routeListing.RouteListingSimpleJson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouteListingControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    TestRestTemplate testRestTemplate;
    TestUtils testUtils;
    @Autowired
    RouteListingRepository routeListingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RouteRepository routeRepository;
    @Autowired
    PlaneRepository planeRepository;

    @BeforeEach
    void setUp() {
        testUtils = new TestUtils(port, encoder);
        userRepository.save(testUtils.createAdminUser());
        userRepository.save(testUtils.createSampleUser1());
    }

    @AfterEach
    void tearDown() {
        routeListingRepository.deleteAll();
        routeRepository.deleteAll();
        planeRepository.deleteAll();
    }

    @Test
    void getAllRouteListings_Success() {
        Route route1 = testUtils.createSampleRoute1();
        Route route2 = testUtils.createSampleRoute2();
        routeRepository.saveAll(List.of(route1, route2));

        Plane plane1 = testUtils.createSamplePlane1();
        Plane plane2 = testUtils.createSamplePlane2();
        planeRepository.saveAll(List.of(plane1, plane2));
        RouteListingSimpleJson routeListing1 = new RouteListingSimpleJson(route1.getRouteId(), plane1.getPlaneId(), LocalDateTime.now(), Duration.ofHours(3), 100.0, 5);
        RouteListingSimpleJson routeListing2 = new RouteListingSimpleJson(route2.getRouteId(), plane2.getPlaneId(), LocalDateTime.now(), Duration.ofHours(4), 120.0, 5);

        routeListingRepository.saveAll(List.of(
                new RouteListing(new RouteListingPk(plane1, route1, routeListing1.getDepartureDatetime()), routeListing1.getBasePrice()),
                new RouteListing(new RouteListingPk(plane2, route2, routeListing2.getDepartureDatetime()), routeListing2.getBasePrice())
        ));

        ResponseEntity<RouteListing[]> responseEntity = testRestTemplate
                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
                .getForEntity("/routeListings", RouteListing[].class);

        assertEquals(200, responseEntity.getStatusCodeValue());
        RouteListing[] routeListings = responseEntity.getBody();
        assertNotNull(routeListings);
        assertEquals(2, routeListings.length);
    }

    @Test
    void getAllRouteListings_WrongAuth_Failure() throws Exception {
        URI uri = testUtils.constructUri("routeListings");
        ResponseEntity<RouteListing[]> responseEntity = testRestTemplate
                .withBasicAuth(testUtils.SAMPLE_USERNAME_1, testUtils.SAMPLE_PASSWORD_1)
                .getForEntity(uri, RouteListing[].class);

        assertEquals(403, responseEntity.getStatusCode().value());
    }

    @Test
    void createRouteListing_Success() throws Exception {
        Route route = testUtils.createSampleRoute1();
        Plane plane = testUtils.createSamplePlane1();
        routeRepository.save(route);
        planeRepository.save(plane);

        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(route, plane);

        URI uri = testUtils.constructUri("routeListings/new");
        ResponseEntity<RouteListing> responseEntity = testRestTemplate
                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
                .postForEntity(uri, routeListingSimpleJson, RouteListing.class);

        assertEquals(201, responseEntity.getStatusCode().value());
        Optional<RouteListing> postedRouteListing = routeListingRepository.findById(responseEntity.getBody().getRouteListingPk());
        assertTrue(postedRouteListing.isPresent());
    }

    @Test
    void createRouteListing_Unauthorized() throws Exception {
        Route route = testUtils.createSampleRoute1();
        Plane plane = testUtils.createSamplePlane1();
        routeRepository.save(route);
        planeRepository.save(plane);
        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(route, plane);
        URI uri = testUtils.constructUri("routeListings/new");
        ResponseEntity<RouteListing> responseEntity = testRestTemplate
                .postForEntity(uri, routeListingSimpleJson, RouteListing.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }
}
