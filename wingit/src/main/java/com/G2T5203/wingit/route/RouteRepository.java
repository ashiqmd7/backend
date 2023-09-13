package com.G2T5203.wingit.route;

import com.G2T5203.wingit.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findAllByDepartureDest(String departureDest);
    List<Route> findAllByArrivalDest(String arrivalDest);
}
