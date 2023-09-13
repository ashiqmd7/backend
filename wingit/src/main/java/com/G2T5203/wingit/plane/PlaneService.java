package com.G2T5203.wingit.plane;

import com.G2T5203.wingit.entities.Plane;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaneService {
    private static final Logger logger = LoggerFactory.getLogger(PlaneService.class);

    private final PlaneRepository repo;

    public PlaneService(PlaneRepository repo) {
        this.repo = repo;
    }

    public List<Plane> getAllPlanes() {
        return repo.findAll();
    }

    public Plane getById(String planeId) {
        return repo.findById(planeId).orElse(null);
    }

    @Transactional
    public HttpStatus createPlane(Plane newPlane) {
        try {
            repo.save(newPlane);
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to add new Plane: DataIntegrityViolationException\n" + newPlane.toString());
            logger.debug("Error details: " + e.getLocalizedMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.CREATED;
    }

    @Transactional
    public HttpStatus deletePlaneById(String planeId) {
        try {
            repo.deleteById(planeId);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Failed to delete plane by Id: EmptyResultDataAccessException\n" +
                    "PlaneId that was attempted to be deleted: " + planeId);
            logger.debug("Error details: " + e.getLocalizedMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }

    @Transactional
    public HttpStatus updatePlane(Plane updatedPlane) {
        try {
            boolean planeExists = getById(updatedPlane.getPlaneId()) != null;
            if (planeExists) {
                repo.save(updatedPlane);
            } else {
                logger.error("Plane " + updatedPlane.getPlaneId() + " does not exist!");
                return HttpStatus.BAD_REQUEST;
            }
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to update Plane: DataIntegrityViolationException\n" + updatedPlane.toString());
            logger.debug("Error details: " + e.getLocalizedMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }
}

