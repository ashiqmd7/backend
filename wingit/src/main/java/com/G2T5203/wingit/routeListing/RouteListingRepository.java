package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.entities.RouteListing;
import com.G2T5203.wingit.entities.RouteListingPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteListingRepository extends JpaRepository<RouteListing, RouteListingPk> {
}
