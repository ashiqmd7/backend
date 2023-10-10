package com.G2T5203.wingit.plane;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaneService {
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
    public Plane createPlane(Plane newPlane) {
        if (repo.existsById(newPlane.getPlaneId())) throw new PlaneBadRequestException("PlaneId already exists.");
        return repo.save(newPlane);
    }

    @Transactional
    public void deletePlaneById(String planeId) {
        if (repo.existsById(planeId)) {
            repo.deleteById(planeId);
        } else {
            throw new PlaneNotFoundException(planeId);
        }
    }

    @Transactional
    public Plane updatePlane(Plane updatedPlane) {
        boolean planeExists = repo.existsById(updatedPlane.getPlaneId());
        if (!planeExists) throw new PlaneNotFoundException(updatedPlane.getPlaneId());
        return repo.save(updatedPlane);
    }
}

