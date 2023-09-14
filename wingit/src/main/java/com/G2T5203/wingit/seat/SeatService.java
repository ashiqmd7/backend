package com.G2T5203.wingit.seat;

import com.G2T5203.wingit.entities.Seat;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository repo;

    public SeatService(SeatRepository repo) { this.repo = repo; }

    public List<Seat> getAllSeats() { return repo.findAll(); }

    @Transactional
    public Seat createSeat(Seat newSeat) { return repo.save(newSeat); }
}
