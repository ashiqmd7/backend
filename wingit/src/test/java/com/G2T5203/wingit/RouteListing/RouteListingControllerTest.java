package com.G2T5203.wingit.RouteListing;

import com.G2T5203.wingit.TestUtils;
import com.G2T5203.wingit.entities.Route;
import com.G2T5203.wingit.entities.Plane;
import com.G2T5203.wingit.entities.RouteListing;
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
    private TestRestTemplate testRestTemplate;
    TestUtils testUtils;
    @Autowired
    private RouteListingRepository routeListingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private PlaneRepository planeRepository;

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
    

}
