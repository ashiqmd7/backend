package com.G2T5203.wingit.booking;

import com.G2T5203.wingit.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByWingitUserUsername(String username);
}
