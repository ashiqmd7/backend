package com.G2T5203.wingit.adminUtils;

import com.G2T5203.wingit.WingitApplication;
import com.G2T5203.wingit.booking.BookingRepository;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.routeListing.RouteListingRepository;
import com.G2T5203.wingit.seat.SeatRepository;
import com.G2T5203.wingit.seatListing.SeatListing;
import com.G2T5203.wingit.seatListing.SeatListingRepository;
import com.G2T5203.wingit.user.UserRepository;
import com.G2T5203.wingit.user.WingitUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@RestController
public class AdminUtilsController {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    private final ApplicationContext context;
    private final UserRepository userRepository;
    private final SeatListingRepository seatListingRepository;
    private final RouteListingRepository routeListingRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final PlaneRepository planeRepository;
    private final RouteRepository routeRepository;

    public AdminUtilsController(ApplicationContext context, UserRepository userRepository, SeatListingRepository seatListingRepository, RouteListingRepository routeListingRepository, BookingRepository bookingRepository, SeatRepository seatRepository, PlaneRepository planeRepository, RouteRepository routeRepository) {
        this.context = context;
        this.userRepository = userRepository;
        this.seatListingRepository = seatListingRepository;
        this.routeListingRepository = routeListingRepository;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.planeRepository = planeRepository;
        this.routeRepository = routeRepository;
    }

    private void deleteAllPlanesAndRoutes() {
        seatListingRepository.deleteAll();
        routeListingRepository.deleteAll();
        bookingRepository.deleteAll();
        seatRepository.deleteAll();
        planeRepository.deleteAll();
        routeRepository.deleteAll();
    }

    private boolean isProduction() { return activeProfile.equals("prod"); }

    @PutMapping(path = "/adminUtils/resetPlanesAndRoutesDB")
    public void resetPlanesAndRoutesDB() {
        // Commenting out as this is not working
//        deleteAllPlanesAndRoutes();
//        DatabaseInitializer.initPlanesAndRoutesData(context, isProduction());
    }

    @PutMapping(path = "/adminUtils/resetUsersAndDB")
    public void resetUsersAndPlanesAndRoutesDB() {
        // Commenting out as this is not working.
//        deleteAllPlanesAndRoutes();
//        List<WingitUser> nonAdminUsers = userRepository.findAllByAuthorityRole("ROLE_USER");
//        userRepository.deleteAll(nonAdminUsers);
//
//        DatabaseInitializer.initNonAdminUsersData(context);
//        DatabaseInitializer.initPlanesAndRoutesData(context, isProduction());
    }
}
