package com.G2T5203.wingit.seat;

import com.G2T5203.wingit.entities.Plane;
import com.G2T5203.wingit.entities.Seat;
import com.G2T5203.wingit.entities.SeatPk;
import com.G2T5203.wingit.plane.PlaneNotFoundException;
import com.G2T5203.wingit.plane.PlaneRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatService {
    private final SeatRepository repo;
    private final PlaneRepository planeRepo;

    public SeatService(SeatRepository repo, PlaneRepository planeRepo) {
        this.repo = repo;
        this.planeRepo = planeRepo;
    }

    public List<SeatSimpleJson> getAllSeats() {
        List<Seat> seats = repo.findAll();
        return seats.stream()
                .map(SeatSimpleJson::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Seat createSeat(SeatSimpleJson newSeatSimpleJson) {
        Optional<Plane> retrievedPlane = planeRepo.findById(newSeatSimpleJson.getPlaneId());
        if (retrievedPlane.isEmpty()) throw new PlaneNotFoundException(newSeatSimpleJson.getPlaneId());

        SeatPk seatPk = new SeatPk(retrievedPlane.get(), newSeatSimpleJson.getSeatNumber());
        boolean alreadyExists = repo.existsById(seatPk);
        if (alreadyExists) throw new SeatBadRequestException("Seat already exists.");

        Seat newSeat = new Seat(
                seatPk,
                newSeatSimpleJson.getSeatClass(),
                newSeatSimpleJson.getPriceFactor());
        return repo.save(newSeat);
    }
}
