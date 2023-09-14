package com.G2T5203.wingit.seat;

import com.G2T5203.wingit.entities.Seat;
import com.G2T5203.wingit.entities.SeatPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, SeatPk> {
}
