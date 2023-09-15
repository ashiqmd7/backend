package com.G2T5203.wingit.route;

import com.G2T5203.wingit.entities.Route;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouteControllerTest {
    @LocalServerPort
    private int port;

    private URI constructUri(String path) throws URISyntaxException {
        String baseUrl = "http://localhost:";
        return new URI(baseUrl + port + "/" + path);
    }

    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired RouteRepository routeRepository;
    @AfterEach
    void tearDown() { routeRepository.deleteAll(); }

    @Test
    void getAllRoutes_Empty_Success() throws Exception {
        URI uri = constructUri("routes");
        ResponseEntity<Route[]> responseEntity = testRestTemplate.getForEntity(uri, Route[].class);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(0, responseEntity.getBody().length);
    }

    // TODO: The rest of the tests below are to be implemented.
    @Test
    void getAllRoutes_TwoAdded_Success() {
    }

    @Test
    void getAllRoutesWithDepartureDest() {
    }

    @Test
    void getRoute() {
    }

    @Test
    void createRoute() {
    }

    @Test
    void deleteRoute() {
    }

    @Test
    void updateRoute() {
    }
}