package com.G2T5203.wingit.plane;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class PlaneControllerTest {
    @LocalServerPort
    private int port;
    private final String baseUrl = "http://localhost:";
    private URI constructUri(String path) throws URISyntaxException {
        return new URI(baseUrl + port + "/" + path);
    }
    @Test
    void getAllPlanes() {
    }

    @Test
    void getPlane() {
    }

    @Test
    void createPlane() {
    }

    @Test
    void deletePlane() {
    }

    @Test
    void updatePlane() {
    }
}