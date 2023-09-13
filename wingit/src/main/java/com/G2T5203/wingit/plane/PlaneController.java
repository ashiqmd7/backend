package com.G2T5203.wingit.plane;

import com.G2T5203.wingit.entities.Plane;
import com.G2T5203.wingit.user.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlaneController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final PlaneService service;

    public PlaneController(PlaneService service) {
        this.service = service;
    }

    // GET all planes
    @GetMapping("/planes")
    public List<Plane> getAllPlanes() {
        return service.getAllPlanes();
    }

    // GET a specific plane by planeId
    @GetMapping("/planes/{planeId}")
    public ResponseEntity<Plane> getPlane(@PathVariable String planeId) {
        Plane plane = service.getById(planeId);
        return ResponseEntity.ok(plane);
    }

    // POST to add a new plane
    @PostMapping("/planes/new")
    public ResponseEntity<Plane> createPlane(@RequestBody Plane newPlane) {
        logger.debug("RequestBody JSON: " + newPlane.toString());
        HttpStatus resultingStatus;
        try {
            resultingStatus = service.createPlane(newPlane);
        } catch (UnexpectedRollbackException e) {
            logger.error("Failed to add new User: UnexpectedRollbackException\n" + newPlane.toString());
            logger.debug("Error details: " + e.getLocalizedMessage());
            resultingStatus = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(resultingStatus).build();
    }

    // DELETE a specific plane by planeId
    @DeleteMapping("/planes/delete/{planeId}")
    public ResponseEntity<Void> deletePlane(@PathVariable String planeId) {
        HttpStatus resultingStatus = service.deletePlaneById(planeId);
        return ResponseEntity.status(resultingStatus).build();
    }

    // PUT to update a specific plane by planeId
    @PutMapping("/planes/update/{planeId}")
    public ResponseEntity<Plane> updatePlane(@PathVariable String planeId, @RequestBody Plane updatedPlane) {
        updatedPlane.setPlaneId(planeId);
        HttpStatus resultingStatus;
        resultingStatus = service.updatePlane(updatedPlane);

        return ResponseEntity.status(resultingStatus).build();
    }
}

