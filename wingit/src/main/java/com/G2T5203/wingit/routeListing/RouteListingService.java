package com.G2T5203.wingit.routeListing;

import com.G2T5203.wingit.entities.RouteListing;
import com.G2T5203.wingit.entities.RouteListingPk;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteListingService {
    private final RouteListingRepository repo;

    public RouteListingService(RouteListingRepository repo) { this.repo = repo; }

    public List<RouteListing> getAllRouteListings() { return repo.findAll(); }

    @Transactional
    public RouteListing createRouteListing(RouteListing newRouteListing) {
        return repo.save(newRouteListing);
    }
}
