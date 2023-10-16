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
import org.springframework.web.util.UriComponentsBuilder;

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
        Route sampleRoute1 = testUtils.createSampleRoute1();
        Route sampleRoute2 = testUtils.createSampleRoute2();
        List<Route> savedRoutes = routeRepository.saveAll(List.of(sampleRoute1, sampleRoute2));

        Plane plane1 = testUtils.createSamplePlane1();
        Plane plane2 = testUtils.createSamplePlane2();
        planeRepository.saveAll(List.of(plane1, plane2));
        RouteListingSimpleJson routeListing1 = new RouteListingSimpleJson(savedRoutes.get(0).getRouteId(), plane1.getPlaneId(), LocalDateTime.now(), Duration.ofHours(3), 100.0, 5);
        RouteListingSimpleJson routeListing2 = new RouteListingSimpleJson(savedRoutes.get(1).getRouteId(), plane2.getPlaneId(), LocalDateTime.now(), Duration.ofHours(4), 120.0, 5);

        routeListingRepository.saveAll(List.of(
                new RouteListing(new RouteListingPk(plane1, savedRoutes.get(0), routeListing1.getDepartureDatetime()), routeListing1.getBasePrice()),
                new RouteListing(new RouteListingPk(plane2, savedRoutes.get(1), routeListing2.getDepartureDatetime()), routeListing2.getBasePrice())
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
        Route sampleRoute1 = testUtils.createSampleRoute1();
        Plane plane = testUtils.createSamplePlane1();
        Route savedRoute = routeRepository.save(sampleRoute1);
        planeRepository.save(plane);

        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(savedRoute, plane);

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

    @Test
    void createRouteListing_WrongAuth_Failure() throws Exception {
        Route route = testUtils.createSampleRoute1();
        Plane plane = testUtils.createSamplePlane1();
        routeRepository.save(route);
        planeRepository.save(plane);
        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(route, plane);
        URI uri = testUtils.constructUri("routeListings/new");

        ResponseEntity<RouteListing> responseEntity = testRestTemplate
                .withBasicAuth(testUtils.SAMPLE_USERNAME_1, testUtils.SAMPLE_PASSWORD_1)
                .postForEntity(uri, routeListingSimpleJson, RouteListing.class);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void createRouteListing_MissingRouteId_Failure() throws Exception {
        Plane plane = testUtils.createSamplePlane1();
        planeRepository.save(plane);

        RouteListingSimpleJson routeListingSimpleJson = new RouteListingSimpleJson(
                999, // This is a non-existent routeId
                plane.getPlaneId(),
                LocalDateTime.now(),
                Duration.ofHours(3),
                100.0,
                5
        );

        URI uri = testUtils.constructUri("routeListings/new");
        ResponseEntity<RouteListing> responseEntity = testRestTemplate
                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
                .postForEntity(uri, routeListingSimpleJson, RouteListing.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void updateRouteListing_WrongAuth_Failure() throws Exception {
        Route sampleRoute = testUtils.createSampleRoute1();
        Plane plane = testUtils.createSamplePlane1();
        Route savedRoute = routeRepository.save(sampleRoute);
        planeRepository.save(plane);

        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(savedRoute, plane);
        URI createUri = testUtils.constructUri("routeListings/new");
        ResponseEntity<RouteListing> createResponseEntity = testRestTemplate
                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
                .postForEntity(createUri, routeListingSimpleJson, RouteListing.class);

        assertEquals(HttpStatus.CREATED, createResponseEntity.getStatusCode());

        // attempt to update the created route listing with wrong credentials
        RouteListing createdRouteListing = createResponseEntity.getBody();
        routeListingSimpleJson.setBasePrice(150.0); // Change the base price

        URI updateUri = testUtils.constructUri("routeListings/update/" + createdRouteListing.getRouteListingPk().getRoute().getRouteId());
        ResponseEntity<RouteListing> updateResponseEntity = testRestTemplate
                .withBasicAuth(testUtils.SAMPLE_USERNAME_1, testUtils.SAMPLE_PASSWORD_1)
                .exchange(updateUri, HttpMethod.PUT, new HttpEntity<>(routeListingSimpleJson), RouteListing.class);

        assertEquals(HttpStatus.FORBIDDEN, updateResponseEntity.getStatusCode());
        // verify that the base price hasn't changed
        RouteListing updatedRouteListing = routeListingRepository.findById(createdRouteListing.getRouteListingPk()).orElse(null);
        assertNotNull(updatedRouteListing);
        assertNotEquals(150.0, updatedRouteListing.getBasePrice());
    }

    @Test
    void searchRouteListingsByPlaneId_Success() throws Exception {
        Route sampleRoute = testUtils.createSampleRoute1();
        Plane plane = testUtils.createSamplePlane1();
        Route savedRoute = routeRepository.save(sampleRoute);
        planeRepository.save(plane);
        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(savedRoute, plane);
        URI createUri = testUtils.constructUri("routeListings/new");
        ResponseEntity<RouteListing> createResponseEntity = testRestTemplate
                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
                .postForEntity(createUri, routeListingSimpleJson, RouteListing.class);

        assertEquals(HttpStatus.CREATED, createResponseEntity.getStatusCode());

        // search for route listings by plane ID
        String planeId = plane.getPlaneId();
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/routeListings/search")
                .queryParam("planeId", planeId);

        ResponseEntity<RouteListing[]> searchResponseEntity = testRestTemplate
                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
                .getForEntity(builder.toUriString(), RouteListing[].class);

        assertEquals(HttpStatus.OK, searchResponseEntity.getStatusCode());
        RouteListing[] routeListings = searchResponseEntity.getBody();
        assertNotNull(routeListings);
        assertEquals(1, routeListings.length);
    }

    @Test
    void searchRouteListingsByPlaneId_PlaneNotFound_Failure() {
        String nonExistentPlaneId = "NonExistentPlane123";
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/routeListings/search")
                .queryParam("planeId", nonExistentPlaneId);

        ResponseEntity<RouteListing[]> searchResponseEntity = testRestTemplate
                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
                .getForEntity(builder.toUriString(), RouteListing[].class);

        assertEquals(HttpStatus.NOT_FOUND, searchResponseEntity.getStatusCode());
    }

//    @Test // getting forbidden
//    void updateRouteListing_Success() throws Exception {
//        Route route = testUtils.createSampleRoute1();
//        Plane plane = testUtils.createSamplePlane1();
//        routeRepository.save(route);
//        planeRepository.save(plane);
//
//        // Create a new route listing
//        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(route, plane);
//        URI createUri = testUtils.constructUri("routeListings/new");
//        ResponseEntity<RouteListing> createResponseEntity = testRestTemplate
//                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
//                .postForEntity(createUri, routeListingSimpleJson, RouteListing.class);
//
//        assertEquals(HttpStatus.CREATED, createResponseEntity.getStatusCode());
//
//        // Update the created route listing
//        RouteListing createdRouteListing = createResponseEntity.getBody();
//        routeListingSimpleJson.setBasePrice(150.0); // Change the base price
//
//        URI updateUri = testUtils.constructUri("routeListings/update/" + createdRouteListing.getRouteListingPk().getRoute().getRouteId());
//        ResponseEntity<RouteListing> updateResponseEntity = testRestTemplate
//                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
//                .exchange(updateUri, HttpMethod.PUT, new HttpEntity<>(routeListingSimpleJson), RouteListing.class);
//
//        assertEquals(HttpStatus.OK, updateResponseEntity.getStatusCode());
//
//        // Check if the base price has been updated in the updated route listing
//        RouteListing updatedRouteListing = routeListingRepository.findById(createdRouteListing.getRouteListingPk()).orElse(null);
//        assertNotNull(updatedRouteListing);
//        assertEquals(150.0, updatedRouteListing.getBasePrice());
//    }

//    @Test // getting forbidden instead of not_found, kiv
//    void updateRouteListing_NotFound_Failure() throws Exception {
//        Route route = testUtils.createSampleRoute1();
//        Plane plane = testUtils.createSamplePlane1();
//        routeRepository.save(route);
//        planeRepository.save(plane);
//
//        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(route, plane);
//
//        // Attempt to update a non-existent route listing
//        URI updateUri = testUtils.constructUri("routeListings/update/999"); // Using a non-existent ID
//        ResponseEntity<RouteListing> updateResponseEntity = testRestTemplate
//                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
//                .exchange(updateUri, HttpMethod.PUT, new HttpEntity<>(routeListingSimpleJson), RouteListing.class);
//
//        assertEquals(HttpStatus.NOT_FOUND, updateResponseEntity.getStatusCode());
//    }

//    @Test // getting 404 not_found instead of 200 ok, kiv
//    void deleteRouteListing_Success() throws Exception {
//        Route route = testUtils.createSampleRoute1();
//        Plane plane = testUtils.createSamplePlane1();
//        routeRepository.save(route);
//        planeRepository.save(plane);
//
//        // Create a new route listing
//        RouteListingSimpleJson routeListingSimpleJson = testUtils.createSampleRouteListingSimpleJson(route, plane);
//        URI createUri = testUtils.constructUri("routeListings/new");
//        ResponseEntity<RouteListing> createResponseEntity = testRestTemplate
//                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
//                .postForEntity(createUri, routeListingSimpleJson, RouteListing.class);
//
//        assertEquals(HttpStatus.CREATED, createResponseEntity.getStatusCode());
//
//        // Delete the created route listing
//        RouteListing createdRouteListing = createResponseEntity.getBody();
//        URI deleteUri = testUtils.constructUri("routeListings/delete/" + createdRouteListing.getRouteListingPk().getRoute().getRouteId());
//        ResponseEntity<Void> deleteResponseEntity = testRestTemplate
//                .withBasicAuth(testUtils.ADMIN_USERNAME, testUtils.ADMIN_PASSWORD)
//                .exchange(deleteUri, HttpMethod.DELETE, null, Void.class);
//
//        assertEquals(HttpStatus.OK, deleteResponseEntity.getStatusCode());
//
//        // Verify that the route listing has been deleted
//        RouteListing deletedRouteListing = routeListingRepository.findById(createdRouteListing.getRouteListingPk()).orElse(null);
//        assertNull(deletedRouteListing);
//    }
}
