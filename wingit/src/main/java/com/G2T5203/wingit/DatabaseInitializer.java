package com.G2T5203.wingit;

import com.G2T5203.wingit.entities.Plane;
import com.G2T5203.wingit.entities.Route;
import com.G2T5203.wingit.entities.WingitUser;
import com.G2T5203.wingit.plane.PlaneRepository;
import com.G2T5203.wingit.route.RouteRepository;
import com.G2T5203.wingit.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;

// Class is used only in DEV profile to pre-populate it with data for testing purposes.

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static void Log(String msg) { logger.debug(msg); }
    public static void init(ApplicationContext context) {
        // Initialise WingitUsers
        UserRepository userRepository = context.getBean(UserRepository.class);
        Log("[Add WingitUser]: " + userRepository.save(new WingitUser(
                "goodpassword",
                "Brandon",
                "Choy",
                Date.valueOf(LocalDate.parse("2001-12-04")),
                "brandon.choy.2037@scis.smu.edu.sg",
                "+65 8746 3847",
                "Mr")));
        Log("[Add WingitUser]: " + userRepository.save(new WingitUser(
                "password",
                "Jared",
                "Hong",
                Date.valueOf(LocalDate.parse("1996-10-03")),
                "jared.hong.2034@scis.smu.edu.sg",
                "+65 8455 0750",
                "Mrs")));


        // Initialise Planes
        PlaneRepository planeRepository = context.getBean(PlaneRepository.class);
        Log("[Add Plane]: " + planeRepository.save(new Plane(
                "SQ123",
                100,
                "B777")));
        Log("[Add Plane]: " + planeRepository.save(new Plane(
                "SQ888",
                200,
                "A350")));


        // Initialise Routes
        RouteRepository routeRepository = context.getBean(RouteRepository.class);
        Log("[Add Route]: " + routeRepository.save(new Route(
                "Singapore",
                "Taiwan",
                Duration.ofHours(4).plusMinutes(30) )));
        Log("[Add Route]: " + routeRepository.save(new Route(
                "Taiwan",
                "Singapore",
                Duration.ofHours(4).plusMinutes(45) )));
        Log("[Add Route]: " + routeRepository.save(new Route(
                "Singapore",
                "Japan",
                Duration.ofHours(7).plusMinutes(20) )));
        Log("[Add Route]: " + routeRepository.save(new Route(
                "Japan",
                "Singapore",
                Duration.ofHours(8).plusMinutes(10) )));
    }
}
