package com.G2T5203.wingit.seatListing;

import com.G2T5203.wingit.entities.SeatListing;
import com.G2T5203.wingit.entities.SeatListingPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatListingRepository extends JpaRepository<SeatListing, SeatListingPk> {
}
