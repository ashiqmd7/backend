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
    public Plane getPlane(@PathVariable String planeId) {
        Plane plane = service.getById(planeId);
        if (plane == null) throw new PlaneNotFoundException(planeId);
        return plane;
    }

    // POST to add a new plane
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/planes/new")
    public Plane createPlane(@RequestBody Plane newPlane) {
        try {
            return service.createPlane(newPlane);
        } catch (Exception e) {
            throw new PlaneBadRequestException(e);
        }
    }

    // DELETE a specific plane by planeId
    @DeleteMapping("/planes/delete/{planeId}")
    public void deletePlane(@PathVariable String planeId) {
        try {
            service.deletePlaneById(planeId);
        } catch (PlaneNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PlaneBadRequestException(e);
        }
    }

    // PUT to update a specific plane by planeId
    @PutMapping("/planes/update/{planeId}")
    public Plane updatePlane(@PathVariable String planeId, @RequestBody Plane updatedPlane) {
        if (!planeId.equals(updatedPlane.getPlaneId())) throw new PlaneBadRequestException("Not the same planeId.");

        updatedPlane.setPlaneId(planeId);
        try {
            return service.updatePlane(updatedPlane);
        } catch (PlaneNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PlaneBadRequestException(e);
        }
    }
}

